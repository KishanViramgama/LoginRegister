package com.app.loginregister.ui.login.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.app.loginregister.R
import com.app.loginregister.base.BaseViewModel
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.network.utility.Result
import com.app.loginregister.ui.login.response.LoginRequest
import com.app.loginregister.ui.login.repository.LoginRepository
import com.app.loginregister.util.failMsg
import com.app.loginregister.util.isNetworkConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val context: Context, private val loginRepository: LoginRepository
) : BaseViewModel() {

    /**
     * Sets the default value of the login state to an empty [ResponseData].
     *
     * The initial value of [loginMutableStateFlow] is set to [ResponseData.Empty()],
     * representing an initial state where no login response has been received.
     */

    private val loginMutableStateFlow: MutableStateFlow<ResponseData<Any>> =
        MutableStateFlow(ResponseData.Empty())
    var loginStateFlow: StateFlow<ResponseData<Any>> = loginMutableStateFlow

    /**
     * You can display a progress bar within the `onStart` lifecycle method or at the
     * [login] function's initiation.
     *
     * @see login Start of the login function
     */

    fun login(email: String, password: String) {

        when (val result = checkValidation.loginValidation(email, password)) {
            is Result.Error -> {
                loginMutableStateFlow.value = ResponseData.Error(result.data, result.error)
            }

            is Result.Success -> {
                if (context.isNetworkConnected()) {
                    viewModelScope.launch {
                        loginRepository.getLoginData(LoginRequest(email, password)).onStart {
                            loginMutableStateFlow.value = ResponseData.Loading()
                        }.catch {
                            loginMutableStateFlow.value =
                                ResponseData.Error(null, context.failMsg(error = it.toString()))
                        }.collect {
                            loginMutableStateFlow.value = it
                        }
                    }
                } else {
                    loginMutableStateFlow.value = ResponseData.InternetConnection(
                        context.resources.getString(R.string.internetConnection)
                    )
                }
            }
        }

    }


}