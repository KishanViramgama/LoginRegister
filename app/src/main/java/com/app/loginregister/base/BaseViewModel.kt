package com.app.loginregister.base

import androidx.lifecycle.ViewModel
import com.app.loginregister.network.utility.CheckValidation
import com.app.loginregister.util.MyDataStore
import javax.inject.Inject

open class BaseViewModel : ViewModel() {

    @Inject
    lateinit var checkValidation: CheckValidation

    @Inject
    lateinit var dataStore: MyDataStore

}