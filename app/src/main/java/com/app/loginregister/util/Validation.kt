package com.app.loginregister.util

import android.util.Patterns

fun isValidMail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
