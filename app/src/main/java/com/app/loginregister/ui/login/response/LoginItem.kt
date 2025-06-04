package com.app.loginregister.ui.login.response

import com.app.loginregister.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginItem(
    @SerializedName("data") val data: Data? = null,
) : BaseResponse(), Serializable {

    data class Data(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("email") var email: String = "",
        @SerializedName("profile_picture") var profilePicture: String = ""
    ) : Serializable

}