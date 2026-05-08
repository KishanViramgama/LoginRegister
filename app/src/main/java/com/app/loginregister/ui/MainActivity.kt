package com.app.loginregister.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.app.loginregister.base.BaseActivity
import com.app.loginregister.ui.chat.screen.ChatScreen
import com.app.loginregister.ui.login.screen.LoginScreen
import com.app.loginregister.ui.navigation.AppRoute
import com.app.loginregister.ui.navigation.Navigator
import com.app.loginregister.ui.profile.screen.ProfileScreen
import com.app.loginregister.ui.register.screen.RegisterScreen
import com.app.loginregister.ui.theme.LoginRegisterTheme
import com.app.loginregister.util.MyDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var dataStore: MyDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val isUserLogin by dataStore.isUserLogin.collectAsState(initial = null)
            val userId by dataStore.getUserID.collectAsState(initial = "")

            val startRoute = remember(isUserLogin, userId) {
                if (isUserLogin == true) {
                    AppRoute.Profile(userId)
                } else {
                    AppRoute.Login
                }
            }

            val navigator = remember(startRoute) {
                Navigator(startRoute)
            }

            LoginRegisterTheme {
                LoginRegisterTheme {
                    NavDisplay(
                        backStack = navigator.backStack, // ✅ correct name
                        onBack = { navigator.goBack() },
                        entryProvider = { key ->

                            when (key) {

                                is AppRoute.Login -> NavEntry(key) {
                                    LoginScreen(navigator, dataStore)
                                }

                                is AppRoute.Register -> NavEntry(key) {
                                    RegisterScreen(navigator)
                                }

                                is AppRoute.Profile -> NavEntry(key) {
                                    ProfileScreen(userId, navigator)
                                }

                                is AppRoute.SingleChat -> NavEntry(key) {
                                    ChatScreen(
                                        navigator = navigator,
                                        senderId = key.senderId,
                                        receiverId = key.receiverId,
                                        receiverName = key.receiverName,
                                        receiverImage = key.receiverImage
                                    )
                                }

                                else -> error("Unknown route: $key")
                            }
                        }
                    )
                }
            }
        }
    }
}

