package com.app.loginregister.register.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loginregister.R
import com.app.loginregister.register.repository.RegisterRepository
import com.app.loginregister.util.BaseItem
import com.app.loginregister.util.KeyName
import com.app.loginregister.util.KeyName.email
import com.app.loginregister.util.KeyName.name
import com.app.loginregister.util.KeyName.password
import com.app.loginregister.util.KeyName.profilePicture
import com.app.loginregister.util.ResponseCodeCheck
import com.app.loginregister.util.ResponseData
import com.app.loginregister.util.failMsg
import com.app.loginregister.util.isNetworkConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val context: Context,
    private val registerRepository: RegisterRepository
) : ViewModel() {

    @Inject
    lateinit var responseCodeCheck: ResponseCodeCheck

    val registerState: MutableStateFlow<ResponseData<BaseItem>> =
        MutableStateFlow(ResponseData.Empty())

    fun register(hashMap: HashMap<String, String>) {


        if (context.isNetworkConnected()) {

            viewModelScope.launch {

                val file = File(hashMap[profilePicture].toString())
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val body = MultipartBody.Part.createFormData(profilePicture, file.name, requestFile)
                val name =
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        hashMap[name].toString()
                    )
                val email =
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        hashMap[email].toString()
                    )
                val password =
                    RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        hashMap[password].toString()
                    )

                val hashMapSendData: HashMap<String, RequestBody> = HashMap()
                hashMapSendData[KeyName.name] = name
                hashMapSendData[KeyName.email] = email
                hashMapSendData[KeyName.password] = password

                registerState.value = ResponseData.Loading()
                registerRepository.getData(body, hashMapSendData).catch {
                    registerState.value =
                        ResponseData.Error(null, context.failMsg(error = it.toString()))
                }.collect {
                    registerState.value = responseCodeCheck.getResponseResult(
                        it
                    )
                }
            }

        } else {
            registerState.value =
                ResponseData.InternetConnection(
                    context.resources.getString(R.string.internetConnection)
                )
        }

    }


}