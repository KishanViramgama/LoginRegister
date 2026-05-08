package com.app.loginregister.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.app.loginregister.BuildConfig
import com.app.loginregister.R
import androidx.core.net.toUri
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

/**
 * Validates if the given [target] is a valid email address.
 *
 * @param target The character sequence to validate.
 * @return True if the target is a valid email, false otherwise.
 */
fun isValidEmail(target: CharSequence?): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

/**
 * Returns a failure message based on the build type.
 * In DEBUG mode, it returns the specific [error] message.
 * In non-DEBUG mode, it returns a generic [msg].
 *
 * @param error The detailed error message for debugging.
 * @param msg The user-friendly error message.
 * @return The appropriate error message string.
 */
fun Context.failMsg(error: String, msg: String = resources.getString(R.string.wrong)): String {
    return if (BuildConfig.DEBUG) {
        error
    } else {
        msg
    }
}

/**
 * Checks if the device has an active network connection with internet capability.
 *
 * @return True if connected to the internet, false otherwise.
 */
fun Context.isNetworkConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
    return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}

/**
 * Adds a clickable modifier without the ripple effect.
 *
 * @param onClick The callback to be invoked when the element is clicked.
 * @return A [Modifier] with no-ripple click behavior.
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
infix fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

/**
 * Prints a log message to Logcat if the build is in DEBUG mode.
 *
 * @param tag The tag for the log message.
 * @param value The value to be logged.
 */
fun printLog(tag: String = "Log Information", value: Any) {
    if (BuildConfig.DEBUG) {
        Log.i(tag, "$tag Log =====> 🧐🧐🧐 $value")
    }
}

/**
 * Opens the application details settings for the current app.
 */
fun Context.openSetting() {
    startActivity(
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            ("package:" + BuildConfig.APPLICATION_ID).toUri()
        )
    )
}

/**
 * Applies a shimmer effect to the modifier.
 *
 * @return A [Modifier] with a shimmer animation.
 */
fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = "shimmer"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}
