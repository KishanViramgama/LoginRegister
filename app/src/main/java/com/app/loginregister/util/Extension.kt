package com.app.loginregister.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Patterns
import com.app.loginregister.BuildConfig
import com.app.loginregister.R

fun isValidMail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun Context.failMsg(error: String, msg: String = resources.getString(R.string.wrong)): String {
    return if (BuildConfig.DEBUG) {
        error
    } else {
        msg
    }
}

fun Context.isNetworkConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
    return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}
