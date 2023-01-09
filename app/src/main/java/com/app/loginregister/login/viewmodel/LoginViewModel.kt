package com.app.loginregister.login.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.loginregister.R
import com.app.loginregister.login.item.LoginItem
import com.app.loginregister.login.repository.LoginRepository
import com.app.loginregister.util.Resource
import com.app.loginregister.util.ResponseCodeCheck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val context: Context,
    private val loginRepository: LoginRepository
) : ViewModel() {

    @Inject
    lateinit var responseCodeCheck: ResponseCodeCheck

    private var loginItemMutableLiveData: MutableLiveData<Resource<LoginItem>> = MutableLiveData()
    var loginItemLiveData: LiveData<Resource<LoginItem>> = loginItemMutableLiveData

    fun login(hashMap: HashMap<String, String>) {

        loginItemMutableLiveData.value = Resource.loading(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<LoginItem> = loginRepository.getData(hashMap)
                loginItemMutableLiveData.postValue(
                    responseCodeCheck.getResponseResult(
                        response
                    )
                )
            } catch (e: Exception) {
                Log.d("data_information",e.toString())
                loginItemMutableLiveData.postValue(
                    Resource.error(context.resources.getString(R.string.wrong), null)
                )
            }
        }

    }


}