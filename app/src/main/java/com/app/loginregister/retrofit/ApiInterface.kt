package com.app.loginregister.retrofit

import com.app.loginregister.login.item.LoginItem
import com.app.loginregister.util.BaseItem
import com.app.loginregister.util.BasePath.login
import com.app.loginregister.util.BasePath.profile
import com.app.loginregister.util.BasePath.register
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ApiInterface {

    //Login user
    @POST(login)
    @FormUrlEncoded
    suspend fun login(@FieldMap hashMap: HashMap<String, String>): Response<LoginItem>

    //Register user
    @POST(register)
    @Multipart
    suspend fun register(
        @Part image: MultipartBody.Part,
        @PartMap hashMap: HashMap<String, RequestBody>
    ): Response<BaseItem>

    //User profile
    @POST(profile)
    @FormUrlEncoded
    suspend fun profile(@FieldMap hashMap: HashMap<String, String>): Response<LoginItem>

}