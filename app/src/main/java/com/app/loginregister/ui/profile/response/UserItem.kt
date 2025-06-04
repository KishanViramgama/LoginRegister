package com.app.loginregister.ui.profile.response

import com.app.loginregister.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class UserItemResponse(
    @SerializedName("data") val data: MutableList<UserItem> = arrayListOf()
) : BaseResponse()

data class UserItem(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("password") val password: String = "",
    @SerializedName("image") val image: String = ""
)