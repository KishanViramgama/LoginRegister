package com.app.loginregister.login.repository

import com.app.loginregister.login.item.LoginItem
import com.app.loginregister.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getData(hashMap: HashMap<String, String>): Flow<Response<LoginItem>> {
        return flow {
            val login = apiInterface.login(hashMap)
            emit(login)
        }.flowOn(Dispatchers.IO)
    }

}