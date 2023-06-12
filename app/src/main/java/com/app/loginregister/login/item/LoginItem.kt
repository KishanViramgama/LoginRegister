package com.app.loginregister.login.item

import com.app.loginregister.util.BaseItem
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginItem(
    @SerializedName("data") val data: Data? = null,
) : BaseItem(), Serializable {

    data class Data(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("email") var email: String = "",
        @SerializedName("profile_picture") var profilePicture: String = ""
    ) : Serializable

}