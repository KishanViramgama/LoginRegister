package com.app.loginregister.ui.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.loginregister.network.socket.SocketState
import com.app.loginregister.ui.chat.response.ChatMessage
import com.app.loginregister.ui.chat.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _recipientOnlineStatus = MutableStateFlow(false)
    val recipientOnlineStatus: StateFlow<Boolean> = _recipientOnlineStatus.asStateFlow()

    val connectionState: StateFlow<SocketState> = repository.connectionState

    private var currentUserId: String = ""
    private var recipientId: String = ""

    fun initChat(senderId: String, receiverId: String) {
        if (this.currentUserId == senderId && this.recipientId == receiverId) return
        
        this.currentUserId = senderId
        this.recipientId = receiverId
        
        repository.connect(senderId, receiverId)

        viewModelScope.launch {
            repository.incomingMessages.collect { dto ->
                if (dto.messageType == "STATUS") {
                    _recipientOnlineStatus.value = dto.text == "ONLINE"
                    return@collect
                }

                // Avoid duplicates between optimistic UI and server broadcast
                val isAlreadyInList = _messages.value.any { 
                    it.message == dto.text && it.time == formatTimestamp(dto.timestamp)
                }

                if (!isAlreadyInList) {
                    val newMessage = ChatMessage(
                        message = dto.text,
                        isMine = dto.senderId == currentUserId,
                        time = formatTimestamp(dto.timestamp)
                    )
                    _messages.value = _messages.value + newMessage
                }
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || currentUserId.isEmpty()) return

        // 1. Send raw string to server
        repository.sendMessage(text)

        // 2. Optimistic Update: Show it in the UI immediately
        val timestamp = System.currentTimeMillis()
        val myMessage = ChatMessage(
            message = text,
            isMine = true,
            time = formatTimestamp(timestamp)
        )
        _messages.value = _messages.value + myMessage
    }

    private fun formatTimestamp(timestamp: Long): String {
        return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))
    }

    override fun onCleared() {
        super.onCleared()
        repository.disconnect()
    }
}
