package com.app.loginregister.ui.login.repository

import android.content.Context
import com.app.loginregister.ui.login.response.LoginItem
import com.app.loginregister.ui.login.response.LoginRequest
import com.app.loginregister.network.domain.ApiInterface
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.network.utility.getResponseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiInterface: ApiInterface, private val context: Context,) {

    fun getLoginData(loginRequest: LoginRequest): Flow<ResponseData<LoginItem>> {
        return flow {
            val login = apiInterface.login(loginRequest)
            emit(getResponseResult(login, context))
        }.flowOn(Dispatchers.IO)
    }

}