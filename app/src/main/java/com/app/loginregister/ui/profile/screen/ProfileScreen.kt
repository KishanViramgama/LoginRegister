package com.app.loginregister.ui.profile.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.loginregister.R
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.ui.navigation.AppRoute
import com.app.loginregister.ui.navigation.Navigator
import com.app.loginregister.ui.profile.response.UserItem
import com.app.loginregister.ui.profile.response.UserItemResponse
import com.app.loginregister.ui.profile.viewmodel.ProfileViewModel
import com.app.loginregister.ui.widget.MarginVertical
import com.app.loginregister.ui.widget.MyText
import com.app.loginregister.ui.widget.ShowLoader
import com.app.loginregister.ui.widget.ShowMyDialog
import com.app.loginregister.util.MyDataStore
import com.app.loginregister.util.shimmerEffect
import com.app.loginregister.util.style.text14Regular
import com.app.loginregister.util.style.text18Bold
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(
    userId: String,
    navigator: Navigator,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    //Show loading
    var isRefreshing by remember { mutableStateOf(false) }
    var isShowLoading by remember { mutableStateOf(false) }

    val userItem = remember { mutableStateListOf<UserItem>() }

    //Show dialog
    var isShowDialog by remember { mutableStateOf(false) }

    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        viewModel.profileStateFlow.collect {
            when (it) {
                is ResponseData.Success -> {
                    if (it.data is UserItemResponse) {
                        userItem.clear()
                        userItem.addAll(it.data.data)
                    }
                    isShowLoading = false
                    isRefreshing = false
                }

                is ResponseData.Loading -> {
                    // Only show full screen loader if not refreshing
                    if (!isRefreshing) {
                        isShowLoading = true
                    }
                }

                is ResponseData.Error -> {
                    isShowLoading = false
                    isRefreshing = false
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }

                is ResponseData.InternetConnection -> {
                    isRefreshing = false
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }

                is ResponseData.Empty -> {
                    isShowLoading = false
                    isRefreshing = false
                }

                is ResponseData.Exception -> {
                    isShowLoading = false
                    isRefreshing = false
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        if (userItem.isEmpty()) {
            viewModel.getUserProfileData()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        MyText(
                            text = stringResource(R.string.userList),
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .weight(1f)
                        )
                        MyText(
                            text = stringResource(R.string.logOut),
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .clickable(onClick = { isShowDialog = true })
                        )
                    }
                }
            )

        }) { paddingValues ->
            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    viewModel.getUserProfileData()
                },
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        if ((isShowLoading || isRefreshing) && userItem.isEmpty()) {
                            items(10) {
                                ShimmerUserItem()
                            }
                        } else {
                            itemsIndexed(userItem) { index, it ->
                                if (index == 0) {
                                    MarginVertical(height = 10.dp)
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp, end = 10.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .clickable {
                                            navigator.navigate(
                                                AppRoute.SingleChat(
                                                    senderId = userId,
                                                    receiverId = it.id,
                                                    receiverName = it.name,
                                                    receiverImage = it.image
                                                )
                                            )
                                        }
                                ) {
                                    GlideImage(
                                        model = it.image,
                                        contentDescription = stringResource(R.string.app_name),
                                        failure = placeholder(R.mipmap.ic_launcher),
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .padding(10.dp)
                                            .clip(CircleShape)
                                    ) {
                                        it.placeholder(R.mipmap.ic_launcher)
                                    }
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 10.dp, end = 10.dp)
                                            .fillMaxWidth(1f)
                                    ) {
                                        MyText(
                                            text = it.name,
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            style = text18Bold()
                                        )
                                        MarginVertical(height = 2.dp)
                                        MyText(
                                            text = it.email,
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            style = text14Regular()
                                        )
                                        MarginVertical(height = 2.dp)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(10.dp)
                                                    .clip(CircleShape)
                                                    .background(if (it.isOnline) Color.Green else Color.Gray)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            MyText(
                                                text = if (it.isOnline) "Online" else "Offline",
                                                style = text14Regular().copy(
                                                    fontSize = 12.sp,
                                                    color = if (it.isOnline) Color.Green else Color.Gray
                                                )
                                            )
                                        }
                                    }
                                }
                                MarginVertical(height = 10.dp)
                            }
                        }
                    }
                }

                if (isShowDialog) {
                    ShowMyDialog(
                        yes = {
                            isShowDialog = false
                            viewModel.logout {
                                navigator.replaceAll(AppRoute.Login)
                            }
                        },
                        no = { isShowDialog = false },
                        title = stringResource(R.string.app_name),
                        msg = stringResource(R.string.logoutMSG)
                    )
                }
                ShowLoader(isShowLoading && userItem.isNotEmpty())
            }
        }
    }
}

@Composable
fun ShimmerUserItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = MaterialTheme.shapes.medium
            )
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(10.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 10.dp, end = 10.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp)
                    .shimmerEffect()
            )
            MarginVertical(height = 5.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(15.dp)
                    .shimmerEffect()
            )
            MarginVertical(height = 5.dp)
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .shimmerEffect()
            )
        }
    }
}
