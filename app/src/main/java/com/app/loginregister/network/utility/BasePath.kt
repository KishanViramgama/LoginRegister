package com.app.loginregister.network.utility

/**
 * Object containing the base URL and endpoint constants for the network requests.
 */
object BasePath {

    /**
     * Flag to determine whether to use Firebase or MySQL endpoints.
     * Set to true to use Firebase, false for MySQL.
     */
    const val IS_FIREBASE = true

    /**
     * The base URL for the API.
     */
    private const val BASE = "http://XXX.XXX.X.XXX:XXXX/"

    /**
     * The complete base URL including the specific path for the database type.
     */
    val BASE_URL = if (IS_FIREBASE) BASE else "${BASE}mySql/"

    /**
     * The dynamic path for the Chat WebSocket based on the database type.
     */
    private val CHAT_PATH = if (IS_FIREBASE) "chat" else "mysql/chat"

    /**
     * The base URL for WebSockets.
     */
    const val BASE_WS_URL = "ws://XXX.XXX.X.XXX:XXXX"

    /**
     * Returns the full WebSocket URL for a specific chat room.
     */
    fun getChatWsUrl(senderId: String, receiverId: String): String {
        return "$BASE_WS_URL/$CHAT_PATH/$senderId/$receiverId"
    }

    /**
     * Endpoint for user login.
     */
    const val LOGIN = "login"

    /**
     * Endpoint for user registration.
     */
    const val REGISTER = "register"

    /**
     * Endpoint for retrieving users.
     */
    const val USERS = "users"

}
