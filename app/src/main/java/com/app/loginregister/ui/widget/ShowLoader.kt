package com.app.loginregister.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.loginregister.R
import com.app.loginregister.util.style.text14Regular


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
                    .background(Color.Unspecified, shape = RoundedCornerShape(8.dp))
            ) {
                Column {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 20.dp)
                    )
                    MyText(
                        text = stringResource(R.string.loading),
                        style = text14Regular(),
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