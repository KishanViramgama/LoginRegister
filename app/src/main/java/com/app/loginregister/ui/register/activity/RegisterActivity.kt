package com.app.loginregister.ui.register.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.loginregister.BuildConfig
import com.app.loginregister.R
import com.app.loginregister.base.BaseActivity
import com.app.loginregister.base.BaseResponse
import com.app.loginregister.network.utility.RegisterError
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.ui.register.viewmodel.RegisterViewModel
import com.app.loginregister.ui.theme.LoginRegisterTheme
import com.app.loginregister.ui.widget.CustomBottomSheetGallery
import com.app.loginregister.ui.widget.CustomDialog
import com.app.loginregister.ui.widget.ImagePickerBottomSheet
import com.app.loginregister.ui.widget.MarginVertical
import com.app.loginregister.ui.widget.MyEditTextField
import com.app.loginregister.ui.widget.MyText
import com.app.loginregister.ui.widget.ShowLoader
import com.app.loginregister.ui.widget.ShowMyDialog
import com.app.loginregister.util.Method
import com.app.loginregister.util.PathUtil
import com.app.loginregister.util.emailKeyBord
import com.app.loginregister.util.noRippleClickable
import com.app.loginregister.util.openSetting
import com.app.loginregister.util.passwordKeyBord
import com.app.loginregister.util.style.text14Regular
import com.app.loginregister.util.style.text22Bold
import com.app.loginregister.util.textKeyBord
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : BaseActivity() {

    //Full name
    private var name by mutableStateOf("")
    private var nameError by mutableStateOf("")

    //Email
    private var email by mutableStateOf("")
    private var emailError by mutableStateOf("")

    //Password
    private var password by mutableStateOf("")
    private var passwordError by mutableStateOf("")

    private lateinit var registerViewModel: RegisterViewModel

    //Show loading
    private var isShowLoading by mutableStateOf(false)

    private var isImagePickDialog by mutableStateOf(false)

    //Show dialog
    private var isShowDialog by mutableStateOf(false)
    private var dialogMSG: String = ""
    private var imagePath by mutableStateOf("")

    private var isShowDialogSetting by mutableStateOf(false)

    private var cameraImagePath: String? = null

    private var isGallery = false

    private var isShowImagePicker by mutableStateOf(false)

    private lateinit var launcherPermissionMultiple: ActivityResultLauncher<Array<String>>

    private var uri = mutableListOf<Uri?>()


    @Inject
    lateinit var method: Method

    @RequiresApi(Build.VERSION_CODES.N)
    @OptIn(ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        lifecycleScope.launch {
            registerViewModel.registerStateFlow.collect {
                when (it) {

                    is ResponseData.Success -> {
                        isShowLoading = false
                        if (it.data is BaseResponse) {
                            val baseResponse = it.data
                            if (baseResponse.status) {
                                finish()
                                Toast.makeText(
                                    this@RegisterActivity, baseResponse.message, Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                dialogMSG = baseResponse.message
                                isShowDialog = true
                            }
                        }
                    }

                    is ResponseData.Loading -> {
                        isShowLoading = true
                    }

                    is ResponseData.Error -> {

                        isShowLoading = false
                        val data = it.data
                        when (data) {

                            RegisterError.ENTER_FULL_NAME -> {
                                nameError = it.error
                            }

                            RegisterError.ENTER_EMAIL -> {
                                emailError = it.error
                            }

                            RegisterError.ENTER_VALID_EMAIL -> {
                                emailError = it.error
                            }

                            RegisterError.ENTER_PASSWORD -> {
                                passwordError = it.error
                            }

                            else -> {
                                Toast.makeText(this@RegisterActivity, it.error, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                    }

                    is ResponseData.InternetConnection -> {
                        Toast.makeText(this@RegisterActivity, it.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResponseData.Empty -> {}
                    is ResponseData.Exception -> {
                        isShowLoading = false
                        Toast.makeText(this@RegisterActivity, it.error, Toast.LENGTH_SHORT).show()
                    }
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
                        isShowDialogSetting = true
                    } else {
                        isShowDialog = true
                        dialogMSG = resources.getString(R.string.permission)
                    }
                }
            }

        /**
        Multiple permission check
         */
        launcherPermissionMultiple =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->

                if (isGranted.getOrDefault(Manifest.permission.READ_MEDIA_IMAGES, false)) {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } else if (isGranted.getOrDefault(
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED, false
                    )
                ) {
                    uri.clear()
                    uri.addAll(getGrantedImageUris())
                    if (uri.isNotEmpty()) {
                        isShowImagePicker = true
                    } else {
                        dialogMSG =
                            getString(R.string.please_allow_limited_image_access_for_this_app)
                    }
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) && shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                        )
                    ) {
                        isShowDialog = true
                        dialogMSG = resources.getString(R.string.permission)
                    } else {
                        isShowDialogSetting = true
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
                        ConstraintLayout(
                            modifier = Modifier
                                .noRippleClickable {
                                    isImagePickDialog = true
                                }
                                .width(120.dp)
                                .height(120.dp)
                                .align(Alignment.CenterHorizontally)) {
                            val (imageUser, imageEdit, spacer) = createRefs()
                            GlideImage(
                                model = imagePath,
                                contentDescription = getString(R.string.app_name),
                                modifier = Modifier
                                    .constrainAs(imageUser) {}
                                    .width(120.dp)
                                    .height(120.dp)) {
                                it.placeholder(R.drawable.app_logo).circleCrop()
                            }
                            Image(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = resources.getString(R.string.app_name),
                                modifier = Modifier
                                    .width(32.dp)
                                    .height(32.dp)
                                    .constrainAs(imageEdit) {
                                        bottom.linkTo(imageUser.bottom)
                                        end.linkTo(spacer.start)
                                    })
                            Spacer(
                                modifier = Modifier
                                    .width(10.dp)
                                    .constrainAs(spacer) {
                                        bottom.linkTo(imageUser.bottom)
                                        end.linkTo(imageUser.end)
                                    })
                        }
                        MarginVertical(height = 25.dp)
                        MyText(
                            text = resources.getString(R.string.signUpNow),
                            modifier = Modifier.fillMaxWidth(),
                            style = text22Bold().copy(textAlign = TextAlign.Center)
                        )
                        MarginVertical(height = 25.dp)
                        MyText(
                            text = resources.getString(R.string.signUpNowTitle),
                            modifier = Modifier.fillMaxWidth(),
                            style = text14Regular().copy(textAlign = TextAlign.Center)
                        )
                        MarginVertical(height = 25.dp)
                        MyEditTextField(
                            value = name,
                            errorMSg = nameError,
                            keyboardOptions = textKeyBord(),
                            label = stringResource(R.string.fullName),
                        ) {
                            name = it
                        }
                        MyEditTextField(
                            value = email,
                            errorMSg = emailError,
                            keyboardOptions = emailKeyBord(),
                            label = stringResource(R.string.email),
                        ) {
                            email = it
                        }
                        MyEditTextField(
                            value = password,
                            errorMSg = passwordError,
                            keyboardOptions = passwordKeyBord(imeAction = ImeAction.Done),
                            label = stringResource(R.string.password),
                        ) {
                            password = it
                        }
                        ElevatedButton(
                            onClick = {
                                nameError = ""
                                emailError = ""
                                passwordError = ""
                                registerViewModel.register(
                                    name.trim(), email.trim(), password.trim(), imagePath
                                )
                            },
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            MyText(
                                text = resources.getString(R.string.signUp)
                            )
                        }
                        ConstraintLayout(
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            val (textDont, textSignUp) = createRefs()
                            MyText(
                                resources.getString(R.string.alreadyHaveAnAccount),
                                Modifier.constrainAs(textDont) {})
                            MyText(
                                resources.getString(R.string.logIn),
                                modifier = Modifier
                                    .constrainAs(textSignUp) {
                                        start.linkTo(textDont.end)
                                    }
                                    .padding(start = 10.dp))
                        }
                        MyText(
                            text = resources.getString(R.string.orConnect),
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .align(Alignment.CenterHorizontally),
                            style = text14Regular().copy(textAlign = TextAlign.Center)
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
                    }
                    ShowLoader(isShowLoading)
                    if (isShowDialog) {
                        ShowMyDialog(
                            yes = { isShowDialog = false },
                            no = { isShowDialog = false },
                            title = resources.getString(R.string.app_name),
                            msg = dialogMSG,
                            isShowDismiss = false
                        )
                    }
                    if (isImagePickDialog) {
                        CustomBottomSheetGallery(
                            isShowBottomSheet = isImagePickDialog,
                            onDismissRequest = { isImagePickDialog = false },
                            onCameraClick = {
                                isGallery = false
                                isImagePickDialog = false
                                launcherPermission.launch(Manifest.permission.CAMERA)
                            },
                            onGalleryClick = {
                                isGallery = true
                                isImagePickDialog = false
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                    launcherPermissionMultiple.launch(
                                        arrayOf(
                                            Manifest.permission.READ_MEDIA_IMAGES,
                                            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                                        )
                                    )
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    launcherPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                } else {
                                    launcherPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }
                        )
                    }
                    if (isShowImagePicker) {
                        ImagePickerBottomSheet(
                            list = uri,
                            onImageClick = {
                                imagePath = PathUtil.getPath(this, it).toString()
                            },
                            isShowBottomSheet = isShowImagePicker,
                            onDismissRequest = { isShowImagePicker = false }
                        )
                    }
                    if (isShowDialogSetting) {
                        CustomDialog(
                            title = stringResource(R.string.setting),
                            positiveBtnText = stringResource(R.string.ok),
                            negativeBtnText = stringResource(R.string.cancel),
                            des = stringResource(R.string.setting_des),
                            onPositive = {
                                isShowDialogSetting = false
                                openSetting()
                            },
                            onNegative = {
                                isShowDialogSetting = false
                            },
                            onDismiss = {
                                isShowDialogSetting = false
                            })
                    }
                }
            }
        }
    }

    private fun getGrantedImageUris(): MutableList<Uri?> {
        val imagesList = mutableListOf<Uri?>()

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = null
        val selectionArgs = null
        val sortOrder = null

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor: Cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                )
                imagesList.add(contentUri)
            }
        }
        return imagesList
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