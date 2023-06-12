package com.app.loginregister.util

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseItem() : Serializable {
    @SerializedName("status")
    val status: Boolean = false

    @SerializedName("message")
    val message: String = ""
}