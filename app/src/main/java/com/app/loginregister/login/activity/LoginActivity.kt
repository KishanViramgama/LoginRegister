package com.app.loginregister.login.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.loginregister.R
import com.app.loginregister.login.viewmodel.LoginViewModel
import com.app.loginregister.profile.activity.ProfileActivity
import com.app.loginregister.register.activity.RegisterActivity
import com.app.loginregister.ui.theme.LoginRegisterTheme
import com.app.loginregister.util.EditTextInput
import com.app.loginregister.util.Method
import com.app.loginregister.util.MyDataStore
import com.app.loginregister.util.ResponseData
import com.app.loginregister.util.isValidMail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    //Email
    private var email by mutableStateOf("")
    private var emailErrorState by mutableStateOf(false)
    private var validEmailErrorState = true
    private val emailFocusRequester = FocusRequester()

    //Password
    private var password by mutableStateOf("")
    private var passwordErrorState by mutableStateOf(false)
    private val passwordFocusRequester = FocusRequester()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        lifecycleScope.launch {
            loginViewModel.loginState.collect {
                when (it) {

                    is ResponseData.Success -> {
                        isShowLoading = false
                        if (it.data!!.status) {
                                dataStore.insertUserID(it.data.data?.id.toString())
                                dataStore.isUserLogin(true)
                                startActivity(
                                    Intent(
                                        this@LoginActivity, ProfileActivity::class.java
                                    ).putExtra("id", it.data.data?.id.toString())
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
                        Toast.makeText(this@LoginActivity, it.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResponseData.InternetConnection -> {
                        Toast.makeText(this@LoginActivity, it.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResponseData.Empty -> {}
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
                        Image(
                            painter = painterResource(R.drawable.app_logo),
                            modifier = Modifier
                                .width(180.dp)
                                .height(180.dp)
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally),
                            contentDescription = resources.getString(R.string.app_name),
                        )
                        Text(
                            text = resources.getString(R.string.logInNow),
                            modifier = Modifier
                                .padding(top = 25.dp)
                                .fillMaxWidth(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = resources.getString(R.string.logInTitle),
                            modifier = Modifier
                                .padding(top = 25.dp)
                                .fillMaxWidth(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
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
                            modifierData = Modifier.padding(top = 25.dp),
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
                        Text(
                            text = resources.getString(R.string.forgetPassword),
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(),
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Right
                        )
                        ElevatedButton(
                            onClick = {

                                emailErrorState = false
                                passwordErrorState = false

                                if (email.trim() == "") {
                                    validEmailErrorState = true
                                    emailErrorState = true
                                } else if (!isValidMail(email.trim())) {
                                    validEmailErrorState = false
                                    emailErrorState = true
                                } else if (password.trim() == "") {
                                    passwordErrorState = true
                                } else {
                                    val hashMap: HashMap<String, String> = HashMap()
                                    hashMap.apply {
                                        put("email", email.trim())
                                        put("password", password.trim())
                                    }
                                    loginViewModel.login(hashMap)
                                }

                            },
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = resources.getString(R.string.logIn)
                            )
                        }
                        ConstraintLayout(
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            val (textDont, textSignUp, spacer) = createRefs()
                            Text(resources.getString(R.string.dontHaveAnAccount),
                                Modifier.constrainAs(textDont) {})
                            Spacer(modifier = Modifier
                                .width(10.dp)
                                .constrainAs(spacer) {
                                    start.linkTo(textDont.end)
                                })
                            Text(resources.getString(R.string.signUp),
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
                                    }
                            )
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
                                msg = message, isShowDismiss = false
                            )
                        }
                    }
                }
            }
        }
    }
}
