package com.app.loginregister.ui.login.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.app.loginregister.R
import com.app.loginregister.base.BaseActivity
import com.app.loginregister.network.utility.LoginError
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.ui.login.response.LoginItem
import com.app.loginregister.ui.login.viewmodel.LoginViewModel
import com.app.loginregister.ui.profile.activity.ProfileActivity
import com.app.loginregister.ui.register.activity.RegisterActivity
import com.app.loginregister.ui.theme.LoginRegisterTheme
import com.app.loginregister.ui.widget.MarginVertical
import com.app.loginregister.ui.widget.MyEditTextField
import com.app.loginregister.ui.widget.MyText
import com.app.loginregister.ui.widget.ShowLoader
import com.app.loginregister.ui.widget.ShowMyDialog
import com.app.loginregister.util.Method
import com.app.loginregister.util.MyDataStore
import com.app.loginregister.util.emailKeyBord
import com.app.loginregister.util.passwordKeyBord
import com.app.loginregister.util.style.text14Regular
import com.app.loginregister.util.style.text18Normal
import com.app.loginregister.util.style.text22Bold
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    //Email
    private var email by mutableStateOf("")
    private var emailError by mutableStateOf("")

    //Password
    private var password by mutableStateOf("")
    private var passwordError by mutableStateOf("")

    //Show loading
    private var isShowLoading by mutableStateOf(false)

    //Show dialog
    private var isShowDialog by mutableStateOf(false)
    private var message: String = ""

    @Inject
    lateinit var method: Method

    @Inject
    lateinit var dataStore: MyDataStore

    private lateinit var loginViewModel: LoginViewModel

    private var isLoginCheck = true


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            dataStore.isUserLogin.asLiveData().observe(this) {
                if (it == true) {
                    dataStore.getUserID.asLiveData().observe(this) { id ->
                        if (isLoginCheck) {
                            startActivity(
                                Intent(this, ProfileActivity::class.java).putExtra(
                                    "id",
                                    id.toString()
                                )
                            )
                            finishAffinity()
                        }
                    }
                }
            }
            false
        }
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        lifecycleScope.launch {
            loginViewModel.loginStateFlow.collect {
                when (it) {
                    is ResponseData.Success -> {
                        isShowLoading = false
                        val loginResponse = it.data as LoginItem
                        if (loginResponse.status) {
                            isLoginCheck = false
                            dataStore.insertUserID(loginResponse.data?.id.toString())
                            dataStore.isUserLogin(true)
                            startActivity(
                                Intent(
                                    this@LoginActivity, ProfileActivity::class.java
                                ).putExtra("id", loginResponse.data?.id.toString())
                            )
                            finishAffinity()
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
                        Toast.makeText(this@LoginActivity, it.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResponseData.Empty -> {}
                    is ResponseData.Exception -> {
                        isShowLoading = false
                        Toast.makeText(this@LoginActivity, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        setContent {
            LoginRegisterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
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
                            contentDescription = resources.getString(R.string.app_name),
                        )
                        MarginVertical(height = 25.dp)
                        MyText(
                            text = resources.getString(R.string.logInNow),
                            style = text22Bold().copy(textAlign = TextAlign.Center),
                            modifier = Modifier.fillMaxWidth()
                        )
                        MarginVertical(height = 25.dp)
                        MyText(
                            text = resources.getString(R.string.logInTitle),
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
                            text = resources.getString(R.string.forgetPassword),
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = text18Normal().copy(textAlign = TextAlign.Right),
                        )
                        ElevatedButton(
                            onClick = {
                                emailError = ""
                                passwordError = ""
                                loginViewModel.login(email.trim(), password.trim())
                            },
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            MyText(
                                text = resources.getString(R.string.logIn)
                            )
                        }
                        ConstraintLayout(
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            val (textDont, textSignUp, spacer) = createRefs()
                            MyText(
                                resources.getString(R.string.dontHaveAnAccount),
                                Modifier.constrainAs(textDont) {})
                            Spacer(
                                modifier = Modifier
                                    .width(10.dp)
                                    .constrainAs(spacer) {
                                        start.linkTo(textDont.end)
                                    })
                            MyText(
                                resources.getString(R.string.signUp),
                                modifier = Modifier
                                    .clickable {
                                        startActivity(
                                            Intent(
                                                this@LoginActivity, RegisterActivity::class.java
                                            )
                                        )
                                    }
                                    .constrainAs(textSignUp) {
                                        start.linkTo(spacer.end)
                                    })
                        }
                        MyText(
                            text = resources.getString(R.string.orConnect),
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
                        ShowLoader(isShowLoading)
                        if (isShowDialog) {
                            ShowMyDialog(
                                yes = { isShowDialog = false },
                                no = { isShowDialog = false },
                                title = resources.getString(R.string.app_name),
                                msg = message,
                                isShowDismiss = false
                            )
                        }
                    }
                }
            }
        }
    }
}
