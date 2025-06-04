package com.app.loginregister.network.utility

import android.content.Context
import com.app.loginregister.R
import com.app.loginregister.util.isValidEmail
import javax.inject.Inject


open class CheckValidation @Inject constructor(val context: Context) {

    fun loginValidation(email: String, password: String): Result<Any, Any> {
        return if (email.isEmpty()) {
            Result.Error(
                LoginError.ENTER_EMAIL,
                context.getString(R.string.please_enter_email)
            )
        } else if (!isValidEmail(email)) {
            Result.Error(
                LoginError.ENTER_VALID_EMAIL,
                context.getString(R.string.please_enter_password)
            )
        } else if (password.isEmpty()) {
            Result.Error(
                LoginError.ENTER_PASSWORD,
                context.getString(R.string.please_enter_password)
            )
        } else {
            Result.Success(true, "")
        }
    }

    fun registerValidation(name: String, email: String, password: String): Result<Any, Any> {
        return if (name.isEmpty()) {
            Result.Error(
                RegisterError.ENTER_FULL_NAME,
                context.getString(R.string.please_enter_email)
            )
        } else if (email.isEmpty()) {
            Result.Error(
                RegisterError.ENTER_EMAIL,
                context.getString(R.string.please_enter_email)
            )
        } else if (!isValidEmail(email)) {
            Result.Error(
                RegisterError.ENTER_VALID_EMAIL,
                context.getString(R.string.please_enter_password)
            )
        } else if (password.isEmpty()) {
            Result.Error(
                RegisterError.ENTER_PASSWORD,
                context.getString(R.string.please_enter_password)
            )
        } else {
            Result.Success(true, "")
        }
    }

    fun networkError(code: Int): String {
        return when (code) {
            400 -> context.getString(R.string.bad_request)
            401 -> context.getString(R.string.unauthorized)
            403 -> context.getString(R.string.forbidden)
            404 -> context.getString(R.string.not_found)
            405 -> context.getString(R.string.method_not_allowed)
            409 -> context.getString(R.string.conflict)
            422 -> context.getString(R.string.unprocessable_entity)
            500 -> context.getString(R.string.internal_server_error)
            502 -> context.getString(R.string.bad_gateway)
            503 -> context.getString(R.string.service_unavailable)
            504 -> context.getString(R.string.gateway_timeout)
            else -> context.getString(R.string.unknown)
        }
    }

}