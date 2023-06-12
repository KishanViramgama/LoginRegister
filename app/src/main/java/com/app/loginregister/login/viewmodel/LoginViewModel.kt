package com.app.loginregister.login.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loginregister.R
import com.app.loginregister.login.item.LoginItem
import com.app.loginregister.login.repository.LoginRepository
import com.app.loginregister.util.ResponseCodeCheck
import com.app.loginregister.util.ResponseData
import com.app.loginregister.util.failMsg
import com.app.loginregister.util.isNetworkConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val context: Context, private val loginRepository: LoginRepository
) : ViewModel() {

    @Inject
    lateinit var responseCodeCheck: ResponseCodeCheck

    /**
     * Set default empty
     */

    val loginState: MutableStateFlow<ResponseData<LoginItem>> =
        MutableStateFlow(ResponseData.Empty())

    /**
     * You can set loading in onStart flow section or you can set directly. which you want to you are preferred
     */

    fun login(hashMap: HashMap<String, String>) {

        if (context.isNetworkConnected()) {
            viewModelScope.launch {
                loginRepository.getData(hashMap).onStart {
                    loginState.value = ResponseData.Loading()
                }.catch {
                    loginState.value =
                        ResponseData.Error(null, context.failMsg(error = it.toString()))
                }.collect {
                    loginState.value = responseCodeCheck.getResponseResult(
                        it
                    )
                }
            }
        } else {
            loginState.value = ResponseData.InternetConnection(
                context.resources.getString(R.string.internetConnection)
            )
        }

    }


}