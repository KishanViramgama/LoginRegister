package com.app.loginregister.register.repository

import com.app.loginregister.retrofit.ApiInterface
import com.app.loginregister.util.BaseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getData(
        image: MultipartBody.Part,
        hashMap: HashMap<String, RequestBody>
    ): Response<BaseItem> {
        return apiInterface.register(image, hashMap)
    }

}