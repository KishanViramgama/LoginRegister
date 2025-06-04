package com.app.loginregister.util

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.app.loginregister.R

enum class FontEnum(val fontFamily: FontFamily) {
    BOlD(FontFamily(Font(R.font.almarai_bold))),
    EXTRA_BOLD(FontFamily(Font(R.font.almarai_extra_bold))),
    REGULAR(FontFamily(Font(R.font.almarai_regular))),
    LIGHT(FontFamily(Font(R.font.almarai_light)))
}