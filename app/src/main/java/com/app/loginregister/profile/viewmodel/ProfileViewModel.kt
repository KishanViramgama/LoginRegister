package com.app.loginregister.profile.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.loginregister.R
import com.app.loginregister.login.item.LoginItem
import com.app.loginregister.profile.repository.ProfileRepository
import com.app.loginregister.util.Resource
import com.app.loginregister.util.ResponseCodeCheck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val context: Context,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    @Inject
    lateinit var responseCodeCheck: ResponseCodeCheck

    private var profileMutableLiveData: MutableLiveData<Resource<LoginItem>> = MutableLiveData()
    var profileLiveData: LiveData<Resource<LoginItem>> = profileMutableLiveData
    
    fun getUserProfileData(hashMap: HashMap<String,String>){
        profileMutableLiveData.value = Resource.loading(null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<LoginItem> = profileRepository.getUserProfileData(hashMap)
                profileMutableLiveData.postValue(
                    responseCodeCheck.getResponseResult(
                        response
                    )
                )
            } catch (e: Exception) {
                Log.d("data_information",e.toString())
                profileMutableLiveData.postValue(
                    Resource.error(context.resources.getString(R.string.wrong), null)
                )
            }
        }
    }

}