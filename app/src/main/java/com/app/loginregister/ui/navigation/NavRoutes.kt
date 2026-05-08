package com.app.loginregister.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppRoute : NavKey {

    @Serializable
    data object Login : AppRoute

    @Serializable
    data object Register : AppRoute

    @Serializable
    data class Profile(val userId: String) : AppRoute

    @Serializable
    data class SingleChat(
        val senderId: String,
        val receiverId: String,
        val receiverName: String,
        val receiverImage: String
    ) : AppRoute
}