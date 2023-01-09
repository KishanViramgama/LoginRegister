package com.app.loginregister.util

import retrofit2.Response

open class ResponseCodeCheck {

    open suspend fun <T> getResponseResult(response: Response<T>): Resource<T> {
        return if (response.code() == 200) {
            if (response.isSuccessful) {
                Resource.success(response.body())
            } else {
                Resource.error("", response.body())
            }
        } else {
            Resource.error("", response.body())
        }
    }

}