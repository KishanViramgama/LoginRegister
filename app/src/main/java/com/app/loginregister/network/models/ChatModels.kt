package com.app.loginregister.network.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val image: String
)

@Serializable
data class ChatMessageDto(
    val senderId: String,
    val receiverId: String,
    val text: String,
    val timestamp: Long,
    val messageType: String = "TEXT"
)
