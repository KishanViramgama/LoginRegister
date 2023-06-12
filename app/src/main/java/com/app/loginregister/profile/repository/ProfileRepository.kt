package com.app.loginregister.profile.repository

import com.app.loginregister.login.item.LoginItem
import com.app.loginregister.retrofit.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getUserProfileData(hashMap: HashMap<String, String>): Flow<Response<LoginItem>> {
        return flow {
            val profile = apiInterface.profile(hashMap)
            emit(profile)
        }.flowOn(Dispatchers.IO)
    }

}