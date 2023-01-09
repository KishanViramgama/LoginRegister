package com.app.loginregister.login.repository

import com.app.loginregister.login.item.LoginItem
import com.app.loginregister.retrofit.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getData(hashMap: HashMap<String,String>): Response<LoginItem> {
        return apiInterface.login(hashMap)
    }

}