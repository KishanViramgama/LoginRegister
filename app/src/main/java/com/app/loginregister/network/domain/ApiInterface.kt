package com.app.loginregister.network.domain

import com.app.loginregister.base.BaseResponse
import com.app.loginregister.ui.login.response.LoginItem
import com.app.loginregister.ui.login.response.LoginRequest
import com.app.loginregister.ui.profile.response.UserItemResponse
import com.app.loginregister.network.utility.BasePath.LOGIN
import com.app.loginregister.network.utility.BasePath.REGISTER
import com.app.loginregister.network.utility.BasePath.USERS
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ApiInterface {

    /**
     * Login user
     */
    @POST(LOGIN)
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginItem>

    /**
     * Register user
     */
    @POST(REGISTER)
    @Multipart
    suspend fun register(
        @Part image: MultipartBody.Part,
        @PartMap hashMap: HashMap<String, RequestBody>,
    ): Response<BaseResponse>

    /**
     * User profile
     */
    @GET(USERS)
    suspend fun profile(): Response<UserItemResponse>

}