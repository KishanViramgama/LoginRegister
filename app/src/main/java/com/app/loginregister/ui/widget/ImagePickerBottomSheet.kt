package com.app.loginregister.ui.widget

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.loginregister.R
import com.app.loginregister.util.style.text22Bold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    list: List<Uri?>,
    onImageClick: (Uri) -> Unit,
    isShowBottomSheet: Boolean = false,
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
        Column {
            MyText(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        colorResource(R.color.purple_700),
                        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.allowImageTitle),
                style = text22Bold()
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(list) { index, item ->
                    LoadImage(imageUrl = item.toString(), modifier = Modifier.aspectRatio(1f)) {
                        if (item != null) {
                            onImageClick.invoke(item)
                        }
                    }
                }
            }
        }
    }
}