package com.app.loginregister.util

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class EditTextInput {

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun editTextInput(
        textName: String,
        label: String,
        errorMSg: String,
        isError: Boolean,
        focusRequester: FocusRequester,
        keyboardOptions: KeyboardOptions,
        modifierData: Modifier,
        onTextChanged: (String) -> Unit
    ) {

        var name by mutableStateOf(textName)

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                onTextChanged(it)
            },
            modifier = modifierData
                .fillMaxWidth()
                .focusRequester(focusRequester = focusRequester),
            label = {
                Text(label)
            },
            singleLine = true,
            isError = isError,
            keyboardOptions = keyboardOptions,
            trailingIcon = {

            }
        )
        if (isError) {
            Text(
                text = errorMSg,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 5.dp, start = 5.dp, end = 5.dp)
            )
        }
    }

}