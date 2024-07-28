package com.app.loginregister.util

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.loginregister.R
import javax.inject.Inject

class Method @Inject constructor(private val context: Context) {

    @Composable
    fun ShowLoader(isShow: Boolean = false) {
        if (isShow) {
            Dialog(
                onDismissRequest = { },
                DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Box(
                    contentAlignment = Center,
                    modifier = Modifier
                        .background(White, shape = RoundedCornerShape(8.dp))
                ) {
                    Column {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 20.dp)
                        )
                        Text(
                            text = context.resources.getString(R.string.loading),
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(
                                top = 20.dp,
                                bottom = 20.dp,
                                start = 20.dp,
                                end = 20.dp
                            )
                        )
                    }

                }

            }
        }
    }

    @Composable
    fun ShowMyDialog(
        yes: () -> Unit,
        no: () -> Unit,
        title: String,
        msg: String,
        isShowConfirm: Boolean = true,
        isShowDismiss: Boolean = true
    ) {
        AlertDialog(onDismissRequest = {
        },
            title = {
                Text(title)
            },
            text = {
                Text(msg)
            },
            confirmButton = {
                if (isShowConfirm) {
                    TextButton(
                        onClick = yes
                    ) {
                        Text(context.resources.getString(R.string.yes))
                    }
                }
            },
            dismissButton = {
                if (isShowDismiss) {
                    TextButton(
                        onClick = no
                    ) {
                        Text(context.resources.getString(R.string.no))
                    }
                }
            })
    }

    /**
     * Dialog code
     */
    @Composable
    fun CustomDialog(onClickCamera: () -> Unit, onClickImage: () -> Unit, onDismiss: () -> Unit) {
        Dialog(
            onDismissRequest = { onDismiss.invoke()},
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
            ) {
                Row(
                    modifier = Modifier.padding(
                        start = 25.dp,
                        end = 25.dp,
                        top = 20.dp,
                        bottom = 20.dp
                    )
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_camera),
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .clickable { onClickCamera.invoke() },
                        colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = context.resources.getString(R.string.app_name),
                    )
                    Spacer(modifier = Modifier.padding(start = 20.dp))
                    Image(
                        painter = painterResource(R.drawable.ic_photo_library),
                        modifier = Modifier
                            .width(60.dp)
                            .height(60.dp)
                            .clickable { onClickImage.invoke() },
                        colorFilter = ColorFilter.tint(Color.Black),
                        contentDescription = context.resources.getString(R.string.app_name),
                    )
                }
            }
        }
    }

}