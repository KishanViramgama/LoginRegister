package com.app.loginregister.ui.chat.response

data class ChatMessage(
    val message: String,
    val isMine: Boolean,
    val time: String
)
