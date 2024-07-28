package com.app.loginregister.register.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.loginregister.BuildConfig
import com.app.loginregister.R
import com.app.loginregister.register.viewmodel.RegisterViewModel
import com.app.loginregister.ui.theme.LoginRegisterTheme
import com.app.loginregister.util.*
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {

    //Full name
    private var fullName by mutableStateOf("")
    private var fullNameErrorState by mutableStateOf(false)
    private val fullNameFocusRequester = FocusRequester()

    //Email
    private var email by mutableStateOf("")
    private var emailErrorState by mutableStateOf(false)
    private var validEmailErrorState = true
    private val emailFocusRequester = FocusRequester()

    //Password
    private var password by mutableStateOf("")
    private var passwordErrorState by mutableStateOf(false)
    private val passwordFocusRequester = FocusRequester()

    private lateinit var registerViewModel: RegisterViewModel

    //Show loading
    private var isShowLoading by mutableStateOf(false)
    private var isImagePickDialog by mutableStateOf(false)

    //Show dialog
    private var isShowDialog by mutableStateOf(false)
    private var dialogMSG: String = ""

    private var imagePath by mutableStateOf("")

    private var cameraImagePath: String? = null

    private var isGallery = false

    @Inject
    lateinit var method: Method

    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        lifecycleScope.launch {
            registerViewModel.registerState.collect {
                when (it) {

                    is ResponseData.Success -> {
                        isShowLoading = false
                        if (it.data!!.status) {
                            finish()
                            Toast.makeText(
                                this@RegisterActivity, it.data.message, Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            dialogMSG = it.data.message
                            isShowDialog = true
                        }
                    }

                    is ResponseData.Loading -> {
                        isShowLoading = true
                    }

                    is ResponseData.Error -> {
                        isShowLoading = false
                        Toast.makeText(this@RegisterActivity, it.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResponseData.InternetConnection -> {
                        Toast.makeText(this@RegisterActivity, it.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResponseData.Empty -> {}
                }
            }
        }

        // Registers a photo picker activity launcher in single-select mode.
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    imagePath = PathUtil.getPath(this, uri).toString()
                }
            }

        //Camera save image success
        val takePicture =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
                if (success) {
                    // The image was saved into the given Uri -> do something with it
                    imagePath = cameraImagePath!!
                }
            }

        val launcherPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    if (isGallery) {
                        /**
                         * Launch the photo picker and allow the user to choose only images.
                         */
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        takePicture.launch(generateImageUrl())
                    }
                } else {
                    val showRationale: Boolean = if (isGallery) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_MEDIA_IMAGES
                        ) else shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                    } else {
                        shouldShowRequestPermissionRationale(
                            Manifest.permission.CAMERA
                        )
                    }
                    if (!showRationale) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                            )
                        )
                    } else {
                        isShowDialog = true
                        dialogMSG = resources.getString(R.string.permission)
                    }
                }
            }

        setContent {
            LoginRegisterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(modifier = Modifier.height(80.dp))
                        ConstraintLayout(modifier = Modifier
                            .clickable {
                                isImagePickDialog = true
                            }
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .align(Alignment.CenterHorizontally)) {
                            val (imageUser, imageEdit, spacer) = createRefs()
                            GlideImage(model = imagePath,
                                contentDescription = getString(R.string.app_name),
                                modifier = Modifier
                                    .constrainAs(imageUser) {}
                                    .width(180.dp)
                                    .height(180.dp)) {
                                it.placeholder(R.drawable.app_logo).circleCrop()
                            }
                            Image(painter = painterResource(R.drawable.ic_edit),
                                contentDescription = resources.getString(R.string.app_name),
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(40.dp)
                                    .constrainAs(imageEdit) {
                                        bottom.linkTo(imageUser.bottom)
                                        end.linkTo(spacer.start)
                                    })
                            Spacer(modifier = Modifier
                                .width(20.dp)
                                .constrainAs(spacer) {
                                    bottom.linkTo(imageUser.bottom)
                                    end.linkTo(imageUser.end)
                                })
                        }
                        Text(
                            text = resources.getString(R.string.signUpNow),
                            modifier = Modifier
                                .padding(top = 25.dp)
                                .fillMaxWidth(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = resources.getString(R.string.signUpNowTitle),
                            modifier = Modifier
                                .padding(top = 25.dp)
                                .fillMaxWidth(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                        EditTextInput().editTextInput(textName = fullName,
                            label = resources.getString(R.string.fullName),
                            errorMSg = resources.getString(R.string.please_enter_fullName),
                            isError = fullNameErrorState,
                            focusRequester = fullNameFocusRequester,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                            ),
                            modifierData = Modifier.padding(top = 25.dp),
                            onTextChanged = { fullName = it })
                        EditTextInput().editTextInput(textName = email,
                            label = resources.getString(R.string.email),
                            errorMSg = if (validEmailErrorState) resources.getString(R.string.please_enter_email) else resources.getString(
                                R.string.please_enter_valid_email
                            ),
                            isError = emailErrorState,
                            focusRequester = emailFocusRequester,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                            ),
                            modifierData = Modifier.padding(top = 10.dp),
                            onTextChanged = { email = it })
                        EditTextInput().editTextInput(textName = password,
                            label = resources.getString(R.string.password),
                            errorMSg = resources.getString(R.string.please_enter_password),
                            isError = passwordErrorState,
                            focusRequester = passwordFocusRequester,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                            ),
                            modifierData = Modifier.padding(top = 10.dp),
                            onTextChanged = { password = it })
                        ElevatedButton(
                            onClick = {
                                fullNameErrorState = false
                                emailErrorState = false
                                passwordErrorState = false
                                if (fullName.trim() == "") {
                                    fullNameErrorState = true
                                } else if (email.trim() == "") {
                                    validEmailErrorState = true
                                    emailErrorState = true
                                } else if (!isValidMail(email.trim())) {
                                    validEmailErrorState = false
                                    emailErrorState = true
                                } else if (password.trim() == "") {
                                    passwordErrorState = true
                                } else if (imagePath.trim() == "") {
                                    isShowDialog = true
                                    dialogMSG = resources.getString(R.string.please_select_image)
                                } else {
                                    val hashMap: HashMap<String, String> = HashMap()
                                    hashMap.apply {
                                        put("name", fullName.trim())
                                        put("email", email.trim())
                                        put("password", password.trim())
                                        put("profile_picture", imagePath)
                                    }
                                    registerViewModel.register(hashMap)
                                }
                            },
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = resources.getString(R.string.signUp)
                            )
                        }
                        ConstraintLayout(
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            val (textDont, textSignUp) = createRefs()
                            Text(resources.getString(R.string.alreadyHaveAnAccount),
                                Modifier.constrainAs(textDont) {})
                            Text(resources.getString(R.string.logIn),
                                modifier = Modifier
                                    .constrainAs(textSignUp) {
                                        start.linkTo(textDont.end)
                                    }
                                    .padding(start = 10.dp))
                        }
                        Text(
                            text = resources.getString(R.string.orConnect),
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .align(Alignment.CenterHorizontally),
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 25.dp, bottom = 20.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.facebook),
                                contentDescription = resources.getString(R.string.app_name),
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(40.dp)
                            )
                            Image(
                                painter = painterResource(R.drawable.twitter),
                                contentDescription = resources.getString(R.string.app_name),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .width(40.dp)
                                    .height(40.dp)
                            )
                        }
                        method.ShowLoader(isShowLoading)
                        if (isShowDialog) {
                            method.ShowMyDialog(
                                yes = { isShowDialog = false },
                                no = { isShowDialog = false },
                                title = resources.getString(R.string.app_name),
                                msg = dialogMSG,
                                isShowDismiss = false
                            )
                        }
                        if (isImagePickDialog) {
                            method.CustomDialog(onClickCamera = {
                                isGallery = false
                                isImagePickDialog = false
                                launcherPermission.launch(Manifest.permission.CAMERA)
                            }, onClickImage = {
                                isGallery = true
                                isImagePickDialog = false
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    launcherPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                } else {
                                    launcherPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }, onDismiss = {
                                isImagePickDialog = false
                            })
                        }
                    }
                }
            }
        }
    }

    private fun generateImageUrl(): Uri {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS")
        val cal: Calendar = Calendar.getInstance()
        val s: String = sdf.format(cal.time)
        val string = "${filesDir}/Image-$s.jpg"
        cameraImagePath = string
        return FileProvider.getUriForFile(
            this, BuildConfig.APPLICATION_ID + ".fileprovider", File(string)
        )
    }

}