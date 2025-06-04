package com.app.loginregister.ui.register.repository

import android.content.Context
import com.app.loginregister.base.BaseResponse
import com.app.loginregister.network.domain.ApiInterface
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.network.utility.getResponseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RegisterRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val context: Context,
) {

    fun getRegisterData(
        image: MultipartBody.Part,
        hashMap: HashMap<String, RequestBody>,
    ): Flow<ResponseData<BaseResponse>> {
        return flow {
            val login = apiInterface.register(image, hashMap)
            emit(getResponseResult(login, context))
        }.flowOn(Dispatchers.IO)
    }

}