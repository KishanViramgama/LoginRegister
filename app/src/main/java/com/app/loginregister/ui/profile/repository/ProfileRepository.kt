package com.app.loginregister.ui.profile.repository

import android.content.Context
import com.app.loginregister.network.domain.ApiInterface
import com.app.loginregister.ui.profile.response.UserItemResponse
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.network.utility.getResponseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val context: Context
) {

    fun getUserProfileData(): Flow<ResponseData<UserItemResponse>> {
        return flow {
            val profile = apiInterface.profile()
            emit(getResponseResult(profile, context))
        }.flowOn(Dispatchers.IO)
    }

}