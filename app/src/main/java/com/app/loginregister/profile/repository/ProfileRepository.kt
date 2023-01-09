package com.app.loginregister.profile.repository

import com.app.loginregister.login.item.LoginItem
import com.app.loginregister.retrofit.ApiInterface
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiInterface: ApiInterface){

    suspend fun getUserProfileData(hashMap: HashMap<String,String>): Response<LoginItem> {
        return apiInterface.profile(hashMap)
    }

}