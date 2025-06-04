package com.app.loginregister.base

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseResponse() : Serializable {
    @SerializedName("status")
    val status: Boolean = false

    @SerializedName("message")
    val message: String = ""
}