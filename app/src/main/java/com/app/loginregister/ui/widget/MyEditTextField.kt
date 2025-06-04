package com.app.loginregister.ui.widget

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.loginregister.util.style.text14Bold
import com.app.loginregister.util.style.text14Regular


@SuppressLint("UnrememberedMutableState")
@Composable
fun MyEditTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    errorMSg: String = "",
    isError: Boolean = false,
    focusRequester: FocusRequester = FocusRequester(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    shape: RoundedCornerShape = RoundedCornerShape(6.dp),
    isTextStyle: Boolean = false,
    textStyle: TextStyle = TextStyle(),
    isShowLabel: Boolean = true,
    isShowPrefix: Boolean = false,
    placeholder: String = "",
    showPrefix: String = "",
    focusedIndicatorColor: Color = Color.Unspecified,
    unfocusedIndicatorColor: Color = Color.Unspecified,
    focusedContainerColor: Color = Color.Unspecified,
    unfocusedContainerColor: Color = Color.Unspecified,
    disabledContainerColor: Color = Color.Unspecified,
    cursorColor: Color = Color.Unspecified,
    onTextChanged: (String) -> Unit,
) {

    val localTextStyle = if (isTextStyle) {
        textStyle
    } else {
        text14Bold()
    }

    if (isShowLabel) {
        OutlinedTextField(
            value = value,
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = focusRequester),
            onValueChange = {
                onTextChanged(it)
            },
            prefix = {
                if (isShowPrefix) {
                    MyText(
                        text = showPrefix,
                        style = localTextStyle
                    )
                }
            },
            textStyle = localTextStyle,
            label = {
                MyText(label, style = text14Regular())
            },
            placeholder = {
                if (placeholder != "") {
                    MyText(placeholder, style = text14Regular())
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = focusedIndicatorColor,
                unfocusedIndicatorColor = unfocusedIndicatorColor,
                focusedContainerColor = focusedContainerColor,
                unfocusedContainerColor = unfocusedContainerColor,
                disabledContainerColor = disabledContainerColor,
                cursorColor = cursorColor
            ),
            shape = shape,
            singleLine = true,
            isError = isError,
            keyboardOptions = keyboardOptions,
        )
    } else {
        TextField(
            value = value,
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = focusRequester),
            onValueChange = {
                onTextChanged(it)
            },
            textStyle = localTextStyle,
            placeholder = {
                if (placeholder != "") {
                    MyText(placeholder, style = text14Regular())
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = focusedIndicatorColor,
                unfocusedIndicatorColor = unfocusedIndicatorColor,
                focusedContainerColor = focusedContainerColor,
                unfocusedContainerColor = unfocusedContainerColor,
                disabledContainerColor = disabledContainerColor,
                cursorColor = cursorColor
            ),
            shape = shape,
            singleLine = true,
            isError = isError,
            keyboardOptions = keyboardOptions,
        )
    }
    if (isError || errorMSg.isNotEmpty()) {
        MyText(
            text = errorMSg,
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 5.dp, start = 5.dp, end = 5.dp)
        )
    }
}