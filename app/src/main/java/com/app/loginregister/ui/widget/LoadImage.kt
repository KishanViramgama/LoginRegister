package com.app.loginregister.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.app.loginregister.R
import com.app.loginregister.util.noRippleClickable
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LoadImage(
    imageUrl: Any,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    onClickListener: () -> Unit = { },
) {
    GlideImage(
        model = imageUrl,
        modifier = modifier.noRippleClickable { onClickListener.invoke() },
        contentDescription = stringResource(id = R.string.app_name),
        contentScale = contentScale,
        failure = placeholder(R.mipmap.ic_launcher)
    ) {
        it.placeholder(R.mipmap.ic_launcher)
    }
}