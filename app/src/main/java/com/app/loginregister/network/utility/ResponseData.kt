package com.app.loginregister.network.utility

sealed class ResponseData<out T>() {
    class Empty<out T>(val type: T? = null) : ResponseData<T>()
    class Loading<out T>(val type: T? = null) : ResponseData<T>()
    class Success<out T>(val data: T? = null, val type: String = "") : ResponseData<T>()
    class Error<out T>(val data: T? = null, val error: String = "") : ResponseData<T>()
    class InternetConnection<out T>(val error: String = "") : ResponseData<T>()
    class Exception<out T>(val data: T? = null, val code: Int = 0,val type: String = "", val error: String = "") :
        ResponseData<T>()
}
