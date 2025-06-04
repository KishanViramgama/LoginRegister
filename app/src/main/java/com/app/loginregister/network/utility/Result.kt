package com.app.loginregister.network.utility

sealed interface Result<out D, out E> {
    data class Success<out D, out E>(val data: D, val error: E) : Result<D, E>
    data class Error<out D, out E>(val data: D, val error: String) : Result<D, E>
}