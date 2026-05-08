package com.app.loginregister.network.socket

import android.util.Log
import com.app.loginregister.network.models.ChatMessageDto
import com.app.loginregister.network.utility.BasePath
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

sealed class SocketState {
    data object Idle : SocketState()
    data object Connecting : SocketState()
    data object Connected : SocketState()
    data class Error(val message: String) : SocketState()
    data object Disconnected : SocketState()
}

@Singleton
class ChatSocketManager @Inject constructor(
    private val client: HttpClient,
    private val json: Json
) {
    private val TAG = "ChatSocketManager"
    private val socketScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var session: WebSocketSession? = null
    private var connectionJob: Job? = null
    private var currentUrl: String? = null

    private val _connectionState = MutableStateFlow<SocketState>(SocketState.Idle)
    val connectionState = _connectionState.asStateFlow()

    private val _incomingMessages = MutableSharedFlow<ChatMessageDto>()
    val incomingMessages = _incomingMessages.asSharedFlow()

    /**
     * Connects to a specific chat room. If already connected to another room, it disconnects first.
     */
    fun connectToChat(senderId: String, receiverId: String) {
        val url = BasePath.getChatWsUrl(senderId, receiverId)
        
        // If already connecting/connected to this EXACT URL, do nothing
        if (currentUrl == url && (_connectionState.value is SocketState.Connected || _connectionState.value is SocketState.Connecting)) {
            Log.d(TAG, "Already connected/connecting to $url")
            return
        }

        Log.d(TAG, "Switching chat session to: $url")
        
        // Cancel previous connection job and close session
        connectionJob?.cancel()
        currentUrl = url

        connectionJob = socketScope.launch {
            // Close existing session if any
            session?.close()
            session = null
            
            var retryCount = 0
            val maxRetries = 5

            while (isActive) {
                try {
                    _connectionState.value = SocketState.Connecting
                    Log.d(TAG, "Attempting connection to $url")
                    
                    session = client.webSocketSession { url(url) }

                    if (session?.isActive == true) {
                        _connectionState.value = SocketState.Connected
                        Log.d(TAG, "Successfully connected to WebSocket: $url")
                        retryCount = 0
                        
                        session?.incoming
                            ?.consumeAsFlow()
                            ?.filterIsInstance<Frame.Text>()
                            ?.mapNotNull { frame ->
                                val text = frame.readText()
                                Log.d(TAG, "[$url] Raw message received: $text")
                                try {
                                    json.decodeFromString<ChatMessageDto>(text)
                                } catch (e: Exception) {
                                    Log.e(TAG, "Failed to decode message: ${e.message}")
                                    null
                                }
                            }
                            ?.collect { message ->
                                Log.d(TAG, "Emitting message user-wise: $message")
                                _incomingMessages.emit(message)
                            }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Socket Error for $url: ${e.localizedMessage}")
                    _connectionState.value = SocketState.Error(e.localizedMessage ?: "Unknown Error")
                    
                    if (retryCount < maxRetries) {
                        retryCount++
                        val backoff = 2000L * retryCount
                        Log.d(TAG, "Retrying $url in $backoff ms (Attempt $retryCount)")
                        delay(backoff)
                    } else {
                        _connectionState.value = SocketState.Disconnected
                        break
                    }
                } finally {
                    Log.d(TAG, "Connection loop iteration finished for $url")
                    _connectionState.value = SocketState.Disconnected
                    delay(5000)
                }
            }
        }
    }

    /**
     * Sends a raw string message. The server will wrap this into a ChatMessage model.
     */
    fun sendMessage(message: String) {
        socketScope.launch {
            try {
                Log.d(TAG, "Sending raw message: $message")
                session?.send(Frame.Text(message))
            } catch (e: Exception) {
                Log.e(TAG, "Send Error: ${e.message}")
                _connectionState.value = SocketState.Error("Failed to send: ${e.message}")
            }
        }
    }

    fun disconnect() {
        socketScope.launch {
            Log.d(TAG, "Manual disconnect requested")
            connectionJob?.cancel()
            session?.close()
            session = null
            currentUrl = null
            _connectionState.value = SocketState.Disconnected
        }
    }
}
