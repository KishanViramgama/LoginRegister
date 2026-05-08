package com.app.loginregister.ui.profile.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.app.loginregister.R
import com.app.loginregister.base.BaseViewModel
import com.app.loginregister.ui.profile.repository.ProfileRepository
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.util.failMsg
import com.app.loginregister.util.isNetworkConnected
import com.app.loginregister.util.MyDataStore
import kotlinx.coroutines.flow.first
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val context: Context, private val profileRepository: ProfileRepository
) : BaseViewModel() {

    private val profileMutableStateFlow: MutableStateFlow<ResponseData<Any>> =
        MutableStateFlow(ResponseData.Empty())
    var profileStateFlow: StateFlow<ResponseData<Any>> = profileMutableStateFlow

    @Inject lateinit var myDataStore: MyDataStore

    fun getUserProfileData() {

        if (context.isNetworkConnected()) {
            viewModelScope.launch {
                val userId = myDataStore.getUserID.first()
                profileRepository.getUserProfileData(userId)
                    .onStart { profileMutableStateFlow.value = ResponseData.Loading() }.catch {
                        profileMutableStateFlow.value =
                            ResponseData.Error(null, context.failMsg(error = it.toString()))
                    }.collect {
                        profileMutableStateFlow.value = it
                    }
            }
        } else {
            profileMutableStateFlow.value = ResponseData.InternetConnection(
                context.resources.getString(R.string.internetConnection)
            )
        }

    }

    fun clearState() {
        profileMutableStateFlow.value = ResponseData.Empty()
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            myDataStore.logout()
            clearState()
            onSuccess()
        }
    }

}