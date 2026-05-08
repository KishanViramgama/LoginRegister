package com.app.loginregister.ui.register.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.loginregister.BuildConfig
import com.app.loginregister.R
import com.app.loginregister.base.BaseResponse
import com.app.loginregister.network.utility.RegisterError
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.ui.navigation.Navigator
import com.app.loginregister.ui.register.viewmodel.RegisterViewModel
import com.app.loginregister.ui.widget.CustomBottomSheetGallery
import com.app.loginregister.ui.widget.CustomDialog
import com.app.loginregister.ui.widget.ImagePickerBottomSheet
import com.app.loginregister.ui.widget.MarginVertical
import com.app.loginregister.ui.widget.MyEditTextField
import com.app.loginregister.ui.widget.MyText
import com.app.loginregister.ui.widget.ShowLoader
import androidx.compose.foundation.clickable
import com.app.loginregister.ui.widget.ShowMyDialog
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
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RegisterScreen(
    navigator: Navigator,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    //Full name
    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }

    //Email
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    //Password
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    //Show loading
    var isShowLoading by remember { mutableStateOf(false) }

    var isImagePickDialog by remember { mutableStateOf(false) }

    //Show dialog
    var isShowDialog by remember { mutableStateOf(false) }
    var dialogMSG by remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf("") }

    var isShowDialogSetting by remember { mutableStateOf(false) }

    var cameraImagePath by remember { mutableStateOf<String?>(null) }

    var isGallery by remember { mutableStateOf(false) }

    var isShowImagePicker by remember { mutableStateOf(false) }

    val uriList = remember { mutableListOf<Uri?>() }

    LaunchedEffect(Unit) {
        viewModel.registerStateFlow.collect {
            when (it) {
                is ResponseData.Success -> {
                    isShowLoading = false
                    if (it.data is BaseResponse) {
                        val baseResponse = it.data
                        if (baseResponse.status) {
                            navigator.goBack()
                            Toast.makeText(context, baseResponse.message, Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is ResponseData.InternetConnection -> {
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }

                is ResponseData.Empty -> {}
                is ResponseData.Exception -> {
                    isShowLoading = false
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            imagePath = PathUtil.getPath(context, uri).toString()
        }
    }

    //Camera save image success
    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            imagePath = cameraImagePath!!
        }
    }

    val launcherPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            if (isGallery) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                val uri = generateImageUrl(context) { cameraImagePath = it }
                takePicture.launch(uri)
            }
        } else {
            // simplified permission handling for brevity in screen extraction
            // ideally we should handle rationale and settings as in the activity
            isShowDialog = true
            dialogMSG = context.getString(R.string.permission)
        }
    }

    val launcherPermissionMultiple = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
        if (isGranted.getOrDefault(Manifest.permission.READ_MEDIA_IMAGES, false)) {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else if (isGranted.getOrDefault(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED, false)) {
            uriList.clear()
            uriList.addAll(getGrantedImageUris(context))
            if (uriList.isNotEmpty()) {
                isShowImagePicker = true
            } else {
                dialogMSG = context.getString(R.string.please_allow_limited_image_access_for_this_app)
                isShowDialog = true
            }
        } else {
            isShowDialogSetting = true
        }
    }

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
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .constrainAs(imageUser) {}
                        .width(120.dp)
                        .height(120.dp)) {
                    it.placeholder(R.drawable.app_logo).circleCrop()
                }
                Image(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = stringResource(R.string.app_name),
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
                text = stringResource(R.string.signUpNow),
                modifier = Modifier.fillMaxWidth(),
                style = text22Bold().copy(textAlign = TextAlign.Center)
            )
            MarginVertical(height = 25.dp)
            MyText(
                text = stringResource(R.string.signUpNowTitle),
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
                    viewModel.register(
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
                    text = stringResource(R.string.signUp)
                )
            }
            ConstraintLayout(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                val (textDont, textSignUp) = createRefs()
                MyText(
                    stringResource(R.string.alreadyHaveAnAccount),
                    Modifier.constrainAs(textDont) {})
                MyText(
                    stringResource(R.string.logIn),
                    modifier = Modifier
                        .constrainAs(textSignUp) {
                            start.linkTo(textDont.end)
                        }
                        .padding(start = 10.dp)
                        .clickable { navigator.goBack() })
            }
            MyText(
                text = stringResource(R.string.orConnect),
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
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
                Image(
                    painter = painterResource(R.drawable.twitter),
                    contentDescription = stringResource(R.string.app_name),
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
                title = stringResource(R.string.app_name),
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
                list = uriList,
                onImageClick = {
                    imagePath = PathUtil.getPath(context, it).toString()
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
                    context.openSetting()
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

private fun getGrantedImageUris(context: android.content.Context): MutableList<Uri?> {
    val imagesList = mutableListOf<Uri?>()

    val projection = arrayOf(MediaStore.Images.Media._ID)

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        null
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

@SuppressLint("SimpleDateFormat")
private fun generateImageUrl(context: android.content.Context, onPathGenerated: (String) -> Unit): Uri {
    val sdf = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss:SSS")
    val cal: Calendar = Calendar.getInstance()
    val s: String = sdf.format(cal.time)
    val string = "${context.filesDir}/Image-$s.jpg"
    onPathGenerated(string)
    return FileProvider.getUriForFile(
        context, BuildConfig.APPLICATION_ID + ".fileprovider", File(string)
    )
}
