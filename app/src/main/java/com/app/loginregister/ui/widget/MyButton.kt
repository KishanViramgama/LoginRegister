package com.app.loginregister.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.loginregister.R
import com.app.loginregister.util.FontEnum
import com.app.loginregister.util.noRippleClickable

@Composable
@Preview
fun MyButton(
    text: String = stringResource(R.string.app_name),
    isActive: Boolean = false,
    buttonActiveColor: Color = Color.Unspecified,
    buttonDeActiveColor: Color = Color.Unspecified,
    texActiveColor: Color = Color.Unspecified,
    texDeActiveColor: Color = Color.Unspecified,
    modifier: Modifier = Modifier,
    radius: Dp = 6.dp,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .noRippleClickable {
                if (isActive) {
                    onClick()
                }
            }
            .background(
                color = if (isActive) buttonActiveColor else buttonDeActiveColor,
                shape = RoundedCornerShape(radius)
            )
            .padding(18.dp)
            .fillMaxWidth()) {
        MyText(
            text = text,
            textAlign = TextAlign.Center,
            color = if (isActive) texActiveColor else texDeActiveColor,
            fontFamily = FontEnum.BOlD,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}