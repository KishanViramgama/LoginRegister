package com.app.loginregister.network.utility

import android.content.Context
import com.app.loginregister.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response

fun <T> getResponseResult(
    response: Response<T>,
    context: Context,
    type: String = "",
): ResponseData<T> {
    return when (response.code()) {
        200, 201 -> {
            if (response.isSuccessful) {
                ResponseData.Success(response.body(), type)
            } else {
                ResponseData.Error(null, context.getString(R.string.wrong))
            }
        }

        else -> {
            val errorBody = response.errorBody()?.string() // Convert errorBody to string
            val errorMessage = extractErrorMessage(errorBody, context, response.code())

            ResponseData.Error(null, errorMessage)
        }
    }
}

/**
 * Extracts error message from error response body.
 */
private fun extractErrorMessage(errorBody: String?, context: Context, code: Int): String {
    return try {
        errorBody?.let {
            val jsonElement = Gson().fromJson(it, JsonObject::class.java)
            jsonElement.get("message")?.asString ?: CheckValidation(context).networkError(code)
        } ?: CheckValidation(context).networkError(code)
    } catch (e: Exception) {
        CheckValidation(context).networkError(code)
    }
}
