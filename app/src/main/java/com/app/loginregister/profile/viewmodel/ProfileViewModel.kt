package com.app.loginregister.profile.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loginregister.R
import com.app.loginregister.login.item.LoginItem
import com.app.loginregister.profile.repository.ProfileRepository
import com.app.loginregister.util.ResponseCodeCheck
import com.app.loginregister.util.ResponseData
import com.app.loginregister.util.failMsg
import com.app.loginregister.util.isNetworkConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val context: Context, private val profileRepository: ProfileRepository
) : ViewModel() {

    @Inject
    lateinit var responseCodeCheck: ResponseCodeCheck

    val profileState: MutableStateFlow<ResponseData<LoginItem>> =
        MutableStateFlow(ResponseData.Empty())

    fun getUserProfileData(hashMap: HashMap<String, String>) {

        if (context.isNetworkConnected()) {
            viewModelScope.launch {
                profileState.value = ResponseData.Loading()
                profileRepository.getUserProfileData(hashMap).catch {
                    profileState.value =
                        ResponseData.Error(null, context.failMsg(error = it.toString()))
                }.collect {
                    profileState.value = responseCodeCheck.getResponseResult(
                        it
                    )
                }
            }
        } else {
            profileState.value = ResponseData.InternetConnection(
                context.resources.getString(R.string.internetConnection)
            )
        }

    }

}