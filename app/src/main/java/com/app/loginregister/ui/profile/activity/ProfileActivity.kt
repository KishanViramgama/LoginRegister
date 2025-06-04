package com.app.loginregister.ui.profile.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.loginregister.R
import com.app.loginregister.base.BaseActivity
import com.app.loginregister.network.utility.ResponseData
import com.app.loginregister.ui.login.activity.LoginActivity
import com.app.loginregister.ui.profile.response.UserItem
import com.app.loginregister.ui.profile.response.UserItemResponse
import com.app.loginregister.ui.profile.viewmodel.ProfileViewModel
import com.app.loginregister.ui.theme.LoginRegisterTheme
import com.app.loginregister.ui.widget.MarginVertical
import com.app.loginregister.ui.widget.MyText
import com.app.loginregister.ui.widget.ShowLoader
import com.app.loginregister.ui.widget.ShowMyDialog
import com.app.loginregister.util.Method
import com.app.loginregister.util.MyDataStore
import com.app.loginregister.util.style.text14Regular
import com.app.loginregister.util.style.text18Bold
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : BaseActivity() {

    private lateinit var id: String
    private lateinit var profileViewModel: ProfileViewModel

    //Show loading
    private var isShowLoading by mutableStateOf(false)

    private lateinit var userItem: MutableList<UserItem>


    @Inject
    lateinit var myDataStore: MyDataStore

    @Inject
    lateinit var method: Method

    //Show dialog
    private var isShowDialog by mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = intent.getStringExtra("id").toString()

        userItem = mutableStateListOf()

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        profileViewModel.getUserProfileData()

        lifecycleScope.launch {
            profileViewModel.profileStateFlow.collect {
                when (it) {
                    is ResponseData.Success -> {
                        if (it.data is UserItemResponse) {
                            userItem.addAll(it.data.data)
                        }
                        isShowLoading = false
                    }

                    is ResponseData.Loading -> {
                        isShowLoading = true
                    }

                    is ResponseData.Error -> {
                        isShowLoading = false
                        Toast.makeText(this@ProfileActivity, it.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResponseData.InternetConnection -> {
                        Toast.makeText(this@ProfileActivity, it.error, Toast.LENGTH_SHORT).show()
                    }

                    is ResponseData.Empty -> {}
                    is ResponseData.Exception -> {
                        isShowLoading = false
                    }
                }

            }
        }

        setContent {
            LoginRegisterTheme {
                // A surface container using the 'background' color from the theme
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
                                        text = resources.getString(R.string.userList),
                                        modifier = Modifier
                                            .padding(start = 20.dp)
                                            .weight(1f)
                                    )
                                    MyText(
                                        text = resources.getString(R.string.logOut),
                                        modifier = Modifier
                                            .padding(end = 20.dp)
                                            .clickable(onClick = { isShowDialog = true })
                                    )
                                }
                            }
                        )

                    }) {
                        Column(modifier = Modifier.padding(it)) {
                            LazyColumn() {
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
                                    ) {
                                        GlideImage(
                                            model = it.image,
                                            contentDescription = getString(R.string.app_name),
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
                                        }
                                    }
                                    MarginVertical(height = 10.dp)
                                }


                            }
                            if (isShowDialog) {
                                ShowMyDialog(
                                    yes = {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            myDataStore.logout()
                                            startActivity(
                                                Intent(
                                                    this@ProfileActivity, LoginActivity::class.java
                                                )
                                            )
                                            finishAffinity()
                                        }
                                    },
                                    no = { isShowDialog = false },
                                    title = resources.getString(R.string.app_name),
                                    msg = resources.getString(R.string.logoutMSG)
                                )
                            }
                            ShowLoader(isShowLoading)
                        }
                    }

                }
            }
        }

    }
}