package com.app.loginregister.ui.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.app.loginregister.util.FontEnum

@Composable
fun MyText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    fontFamily: FontEnum = FontEnum.REGULAR,
    style: TextStyle? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
        style = style ?: TextStyle(
            fontSize = fontSize,
            color = color,
            textAlign = textAlign ?: TextAlign.Start, // Ensures a valid default value
            fontFamily = fontFamily.fontFamily
        )
    )
}






