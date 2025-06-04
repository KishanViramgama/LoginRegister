package com.app.loginregister.util

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

fun passwordKeyBord(imeAction: ImeAction = ImeAction.Next): KeyboardOptions {
    return KeyboardOptions(
        keyboardType = KeyboardType.Password, imeAction = imeAction
    )
}

fun mobileNumberKeyBord(imeAction: ImeAction = ImeAction.Next): KeyboardOptions {
    return KeyboardOptions(
        keyboardType = KeyboardType.Phone, imeAction = imeAction
    )
}

fun numberKeyBord(imeAction: ImeAction = ImeAction.Next): KeyboardOptions {
    return KeyboardOptions(
        keyboardType = KeyboardType.Number, imeAction = imeAction
    )
}

fun emailKeyBord(imeAction: ImeAction = ImeAction.Next): KeyboardOptions {
    return KeyboardOptions(
        keyboardType = KeyboardType.Email, imeAction = imeAction
    )
}

fun textKeyBord(imeAction: ImeAction = ImeAction.Next): KeyboardOptions {
    return KeyboardOptions(
        keyboardType = KeyboardType.Text, imeAction = imeAction
    )
}

fun passKeyBord(imeAction: ImeAction = ImeAction.Next): KeyboardOptions {
    return KeyboardOptions(
        keyboardType = KeyboardType.Password, imeAction = imeAction
    )
}