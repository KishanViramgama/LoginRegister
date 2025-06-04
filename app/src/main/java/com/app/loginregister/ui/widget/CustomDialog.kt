package com.app.loginregister.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.loginregister.R
import com.app.loginregister.util.FontEnum
import com.app.loginregister.util.noRippleClickable
import com.app.loginregister.util.style.text14Regular
import com.app.loginregister.util.style.text18Bold

/**
 * Dialog code
 */
@Composable
@Preview(showBackground = true)
fun CustomDialog(
    title: String = "",
    des: String = "",
    logo: Int = R.drawable.app_logo,
    positiveBtnText: String = "",
    negativeBtnText: String = "",
    onPositive: () -> Unit = { },
    onNegative: () -> Unit = { },
    onDismiss: () -> Unit = { },
) {
    val context = LocalContext.current
    Dialog(
        onDismissRequest = { onDismiss.invoke() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(logo),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.size(66.dp)
                )
                MarginVertical(height = 20.dp)
                if (title != "") {
                    MyText(title, style = text18Bold().copy(textAlign = TextAlign.Center))
                    MarginVertical(height = 10.dp)
                }
                if (des != "") {
                    MyText(des, style = text14Regular().copy(textAlign = TextAlign.Center))
                }
                MarginVertical(height = 22.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val modifierPositive =
                        if (negativeBtnText == "") Modifier.wrapContentWidth() else
                            Modifier
                                .weight(1f)
                    val modifierPositiveText =
                        if (negativeBtnText == "") Modifier
                            .wrapContentWidth()
                            .padding(horizontal = 20.dp) else
                            Modifier
                                .fillMaxWidth()
                    Box(
                        modifier = modifierPositive
                            .noRippleClickable { onPositive.invoke() }
                            .background(
                                color = Color.Unspecified,
                                shape = RoundedCornerShape(18.dp)
                            )
                    ) {
                        MyText(
                            text = positiveBtnText,
                            textAlign = TextAlign.Center,
                            fontFamily = FontEnum.BOlD,
                            fontSize = 16.sp,
                            modifier = modifierPositiveText
                                .padding(18.dp)

                        )
                    }
                    if (negativeBtnText != "") {
                        MarginHorizontal()
                        Box(
                            modifier = Modifier
                                .noRippleClickable {
                                    onNegative.invoke()
                                }
                                .background(
                                    color = Color.Unspecified,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .weight(1f)
                        ) {
                            MyText(
                                text = negativeBtnText,
                                textAlign = TextAlign.Center,
                                fontFamily = FontEnum.BOlD,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp)

                            )
                        }
                    }
                }
            }
        }
    }
}
