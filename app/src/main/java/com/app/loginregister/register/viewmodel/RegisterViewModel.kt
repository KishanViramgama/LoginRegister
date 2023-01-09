package com.app.loginregister.register.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.loginregister.R
import com.app.loginregister.register.repository.RegisterRepository
import com.app.loginregister.util.BaseItem
import com.app.loginregister.util.KeyName
import com.app.loginregister.util.KeyName.email
import com.app.loginregister.util.KeyName.name
import com.app.loginregister.util.KeyName.password
import com.app.loginregister.util.KeyName.profilePicture
import com.app.loginregister.util.Resource
import com.app.loginregister.util.ResponseCodeCheck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val context: Context,
    private val registerRepository: RegisterRepository
) : ViewModel() {

    @Inject
    lateinit var responseCodeCheck: ResponseCodeCheck

    private var registerItemMutableLiveData: MutableLiveData<Resource<BaseItem>> = MutableLiveData()
    var registerItemLiveData: LiveData<Resource<BaseItem>> = registerItemMutableLiveData

    fun register(hashMap: HashMap<String, String>) {

        val file = File(hashMap[profilePicture].toString())
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData(profilePicture, file.name, requestFile)
        val name =
            RequestBody.create(MediaType.parse("multipart/form-data"), hashMap[name].toString())
        val email =
            RequestBody.create(MediaType.parse("multipart/form-data"), hashMap[email].toString())
        val password =
            RequestBody.create(MediaType.parse("multipart/form-data"), hashMap[password].toString())

        val hashMapSendData: HashMap<String, RequestBody> = HashMap()
        hashMapSendData[KeyName.name] = name
        hashMapSendData[KeyName.email] = email
        hashMapSendData[KeyName.password] = password

        registerItemMutableLiveData.value = Resource.loading(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<BaseItem> = registerRepository.getData(body, hashMapSendData)
                registerItemMutableLiveData.postValue(
                    responseCodeCheck.getResponseResult(
                        response
                    )
                )
            } catch (e: Exception) {
                Log.d("data_information", e.toString())
                registerItemMutableLiveData.postValue(
                    Resource.error(context.resources.getString(R.string.wrong), null)
                )
            }
        }

    }


}