package com.app.loginregister.util

import retrofit2.Response

open class ResponseCodeCheck {

    open suspend fun <T> getResponseResult(response: Response<T>): ResponseData<T> {
        return if (response.code() == 200) {
            if (response.isSuccessful) {
                ResponseData.Success(response.body())
            } else {
                ResponseData.Error(response.body(), "")
            }
        } else {
            ResponseData.Error(response.body(), "")
        }
    }

}