package com.app.loginregister.register.repository

import com.app.loginregister.retrofit.ApiInterface
import com.app.loginregister.util.BaseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getData(
        image: MultipartBody.Part,
        hashMap: HashMap<String, RequestBody>
    ): Flow<Response<BaseItem>> {
        return flow {
            val register = apiInterface.register(image, hashMap)
            emit(register)
        }.flowOn(Dispatchers.IO)
    }

}