package com.app.loginregister.ui.widget

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.app.loginregister.R

@Composable
fun ShowMyDialog(
    yes: () -> Unit,
    no: () -> Unit,
    title: String,
    msg: String,
    isShowConfirm: Boolean = true,
    isShowDismiss: Boolean = true
) {
    AlertDialog(
        onDismissRequest = {
        },
        title = {
            MyText(title)
        },
        text = {
            MyText(msg)
        },
        confirmButton = {
            if (isShowConfirm) {
                TextButton(
                    onClick = yes
                ) {
                    MyText(stringResource(R.string.yes))
                }
            }
        },
        dismissButton = {
            if (isShowDismiss) {
                TextButton(
                    onClick = no
                ) {
                    MyText(stringResource(R.string.no))
                }
            }
        })
}