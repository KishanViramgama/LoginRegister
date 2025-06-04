package com.app.loginregister.ui.register.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.app.loginregister.R
import com.app.loginregister.base.BaseViewModel
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.network.utility.Result
import com.app.loginregister.ui.register.repository.RegisterRepository
import com.app.loginregister.util.failMsg
import com.app.loginregister.util.isNetworkConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val context: Context,
    private val registerRepository: RegisterRepository,
) : BaseViewModel() {


    private val registerMutableStateFlow: MutableStateFlow<ResponseData<Any>> =
        MutableStateFlow(ResponseData.Empty())
    var registerStateFlow: StateFlow<ResponseData<Any>> = registerMutableStateFlow

    fun register(name: String, email: String, password: String, image: String) {

        when (val result = checkValidation.registerValidation(name, email, password)) {
            is Result.Error -> {
                registerMutableStateFlow.value = ResponseData.Error(result.data, result.error)
            }

            is Result.Success -> {
                if (context.isNetworkConnected()) {

                    viewModelScope.launch {

                        val body = if (image.isNotEmpty()) {
                            val file = File(image)
                            val requestFile =
                                RequestBody.create(MediaType.parse("multipart/form-data"), file)
                            MultipartBody.Part.createFormData("image", file.name, requestFile)
                        } else {
                            MultipartBody.Part.createFormData(
                                "image",
                                "",
                                RequestBody.create(null, "")
                            )
                        }
                        val name =
                            RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                name
                            )
                        val email =
                            RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                email
                            )
                        val password =
                            RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                password
                            )

                        val hashMapSendData: HashMap<String, RequestBody> = HashMap()
                        hashMapSendData["name"] = name
                        hashMapSendData["email"] = email
                        hashMapSendData["password"] = password

                        registerRepository.getRegisterData(body, hashMapSendData)
                            .onStart {
                                registerMutableStateFlow.value = ResponseData.Loading()
                            }.catch {
                                registerMutableStateFlow.value =
                                    ResponseData.Error(null, context.failMsg(error = it.toString()))
                            }.collect {
                                registerMutableStateFlow.value = it
                            }

                    }

                } else {
                    registerMutableStateFlow.value =
                        ResponseData.InternetConnection(
                            context.resources.getString(R.string.internetConnection)
                        )
                }
            }
        }

    }

}