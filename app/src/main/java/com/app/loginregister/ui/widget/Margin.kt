package com.app.loginregister.ui.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun MarginVertical(modifier: Modifier = Modifier, height: Dp = 10.dp) {
    Spacer(modifier = modifier.height(height))
}

@Composable
fun MarginHorizontal(modifier: Modifier = Modifier,width: Dp = 10.dp) {
    Spacer(modifier = modifier.width(width))
}
