package com.app.loginregister.ui.chat.repository

import com.app.loginregister.network.models.ChatMessageDto
import com.app.loginregister.network.socket.ChatSocketManager
import com.app.loginregister.network.socket.SocketState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val socketManager: ChatSocketManager
) {
    val connectionState: StateFlow<SocketState> = socketManager.connectionState
    val incomingMessages: Flow<ChatMessageDto> = socketManager.incomingMessages

    fun connect(senderId: String, receiverId: String) {
        socketManager.connectToChat(senderId, receiverId)
    }

    fun sendMessage(message: String) {
        socketManager.sendMessage(message)
    }

    fun disconnect() {
        socketManager.disconnect()
    }
}
