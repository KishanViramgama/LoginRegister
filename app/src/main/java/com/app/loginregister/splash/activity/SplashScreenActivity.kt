package com.app.loginregister.splash.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.asLiveData
import com.app.loginregister.login.activity.LoginActivity
import com.app.loginregister.profile.activity.ProfileActivity
import com.app.loginregister.util.MyDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : ComponentActivity() {

    @Inject
    lateinit var myDataStore: MyDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            myDataStore.isUserLogin.asLiveData().observe(this) {
                if (it == true) {
                    myDataStore.getUserID.asLiveData().observe(this) { id ->
                        startActivity(
                            Intent(this, ProfileActivity::class.java).putExtra(
                                "id",
                                id.toString()
                            )
                        )
                    }
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                finishAffinity()
            }
            false
        }
        super.onCreate(savedInstanceState)
    }

}