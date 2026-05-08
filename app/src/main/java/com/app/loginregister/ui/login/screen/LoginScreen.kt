package com.app.loginregister.ui.login.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.loginregister.R
import com.app.loginregister.network.utility.LoginError
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.ui.login.response.LoginItem
import com.app.loginregister.ui.login.viewmodel.LoginViewModel
import com.app.loginregister.ui.navigation.AppRoute
import com.app.loginregister.ui.navigation.Navigator
import com.app.loginregister.ui.widget.MarginVertical
import com.app.loginregister.ui.widget.MyEditTextField
import com.app.loginregister.ui.widget.MyText
import com.app.loginregister.ui.widget.ShowLoader
import com.app.loginregister.ui.widget.ShowMyDialog
import com.app.loginregister.util.MyDataStore
import com.app.loginregister.util.emailKeyBord
import com.app.loginregister.util.passwordKeyBord
import com.app.loginregister.util.style.text14Regular
import com.app.loginregister.util.style.text18Normal
import com.app.loginregister.util.style.text22Bold

@Composable
fun LoginScreen(
    navigator: Navigator, dataStore: MyDataStore, viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //Email
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    //Password
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    //Show loading
    var isShowLoading by remember { mutableStateOf(false) }

    //Show dialog
    var isShowDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loginStateFlow.collect {
            when (it) {
                is ResponseData.Success -> {
                    isShowLoading = false
                    val loginResponse = it.data as LoginItem
                    if (loginResponse.status) {
                        dataStore.insertUserID(loginResponse.data?.id.toString())
                        dataStore.isUserLogin(true)
                        navigator.replaceAll(AppRoute.Profile(loginResponse.data?.id.toString()))
                    } else {
                        isShowDialog = true
                        message = it.data.message
                    }
                }

                is ResponseData.Loading -> {
                    isShowLoading = true
                }

                is ResponseData.Error -> {
                    isShowLoading = false
                    val data = it.data
                    when (data) {
                        LoginError.ENTER_EMAIL -> {
                            emailError = it.error
                        }

                        LoginError.ENTER_VALID_EMAIL -> {
                            emailError = it.error
                        }

                        LoginError.ENTER_PASSWORD -> {
                            passwordError = it.error
                        }

                        else -> {
                            isShowDialog = true
                            message = it.error
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

    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(R.drawable.app_logo),
                modifier = Modifier
                    .width(180.dp)
                    .height(180.dp)
                    .padding(top = 40.dp)
                    .align(Alignment.CenterHorizontally),
                contentDescription = stringResource(R.string.app_name),
            )
            MarginVertical(height = 25.dp)
            MyText(
                text = stringResource(R.string.logInNow),
                style = text22Bold().copy(textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth()
            )
            MarginVertical(height = 25.dp)
            MyText(
                text = stringResource(R.string.logInTitle),
                modifier = Modifier.fillMaxWidth(),
                style = text14Regular().copy(textAlign = TextAlign.Center),
            )
            MarginVertical(height = 25.dp)
            MyEditTextField(
                value = email,
                errorMSg = emailError,
                keyboardOptions = emailKeyBord(),
                label = stringResource(R.string.email),
            ) {
                email = it
            }
            MarginVertical(height = 10.dp)
            MyEditTextField(
                value = password,
                errorMSg = passwordError,
                keyboardOptions = passwordKeyBord(imeAction = ImeAction.Done),
                label = stringResource(R.string.password),
            ) {
                password = it
            }
            MarginVertical(height = 10.dp)
            MyText(
                text = stringResource(R.string.forgetPassword),
                modifier = Modifier.fillMaxWidth(),
                style = text18Normal().copy(textAlign = TextAlign.Right),
            )
            ElevatedButton(
                onClick = {
                    emailError = ""
                    passwordError = ""
                    viewModel.login(email.trim(), password.trim())
                },
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                MyText(
                    text = stringResource(R.string.logIn)
                )
            }
            ConstraintLayout(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                val (textDont, textSignUp, spacer) = createRefs()
                MyText(
                    stringResource(R.string.dontHaveAnAccount), Modifier.constrainAs(textDont) {})
                Spacer(
                    modifier = Modifier
                        .width(10.dp)
                        .constrainAs(spacer) {
                            start.linkTo(textDont.end)
                        })
                MyText(
                    stringResource(R.string.signUp), modifier = Modifier
                        .clickable {
                            navigator.navigate(AppRoute.Register)
                        }
                        .constrainAs(textSignUp) {
                            start.linkTo(spacer.end)
                        })
            }
            MyText(
                text = stringResource(R.string.orConnect),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                style = text18Normal()
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
            ShowLoader(isShowLoading)
            if (isShowDialog) {
                ShowMyDialog(
                    yes = { isShowDialog = false },
                    no = { isShowDialog = false },
                    title = stringResource(R.string.app_name),
                    msg = message,
                    isShowDismiss = false
                )
            }
        }
    }
}
