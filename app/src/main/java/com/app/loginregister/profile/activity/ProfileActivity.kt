package com.app.loginregister.profile.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.loginregister.R
import com.app.loginregister.login.activity.LoginActivity
import com.app.loginregister.login.item.LoginItem
import com.app.loginregister.profile.viewmodel.ProfileViewModel
import com.app.loginregister.ui.theme.LoginRegisterTheme
import com.app.loginregister.util.Method
import com.app.loginregister.util.MyDataStore
import com.app.loginregister.util.ResponseData
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : ComponentActivity() {

    lateinit var id: String
    lateinit var profileViewModel: ProfileViewModel

    //Show loading
    private var isShowLoading by mutableStateOf(false)

    private var loginData by mutableStateOf(LoginItem())

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

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        val hashMap: HashMap<String, String> = HashMap()
        hashMap.apply {
            put("user_id", id)
        }
        profileViewModel.getUserProfileData(hashMap)

        lifecycleScope.launch {
            profileViewModel.profileState.collect {
                when (it) {
                    is ResponseData.Success -> {
                        loginData = it.data!!
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
                }

            }
        }

        setContent {
            LoginRegisterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        GlideImage(
                            model = loginData.data?.profilePicture,
                            contentDescription = getString(R.string.app_name),
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .width(128.dp)
                                .height(128.dp)
                                .align(Alignment.CenterHorizontally)
                                .clickable(onClick = {}),
                        )
                        Text(
                            text = loginData.data?.name.toString(),
                            modifier = Modifier
                                .padding(top = 25.dp)
                                .fillMaxWidth(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                        ElevatedButton(
                            onClick = {
                                isShowDialog = true
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 20.dp, bottom = 20.dp)
                        ) {
                            Text(
                                text = resources.getString(R.string.logOut)
                            )
                        }
                        if (isShowDialog) {
                            method.ShowMyDialog(
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
                    }
                }
            }
        }

    }
}