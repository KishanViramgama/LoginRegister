package com.app.loginregister.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.loginregister.R
import com.app.loginregister.util.noRippleClickable
import com.app.loginregister.util.style.text14Regular
import com.app.loginregister.util.style.text22Bold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun CustomBottomSheetGallery(
    isShowBottomSheet: Boolean = false,
    onCameraClick: () -> Unit = {},
    onGalleryClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    LaunchedEffect(isShowBottomSheet) {
        if (isShowBottomSheet) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = Modifier,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            MyText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.select_option),
                style = text22Bold().copy(textAlign = TextAlign.Center)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable {
                            onCameraClick.invoke()
                        }, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier
                            .background(
                                Color.Unspecified, CircleShape
                            )
                            .padding(20.dp)

                    )
                    MarginVertical(height = 20.dp)
                    MyText(
                        text = stringResource(R.string.take_from_camera),
                        style = text14Regular()
                    )
                }
                MarginHorizontal()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable {
                            onGalleryClick.invoke()
                        }, horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_photo_library),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier
                            .background(
                                Color.Unspecified, CircleShape
                            )
                            .padding(20.dp)

                    )
                    MarginVertical(height = 20.dp)
                    MyText(
                        text = stringResource(R.string.select_from_gallery),
                        style = text14Regular()
                    )
                }

            }
        }
    }
}

