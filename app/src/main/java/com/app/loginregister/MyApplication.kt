package com.app.loginregister

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(), Application.ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()

        DynamicColors.applyToActivitiesIfAvailable(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        //Log.e("data_information", "on create")
                    }

                    Lifecycle.Event.ON_START -> {
                        //Log.e("data_information", "on start")
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        //Log.e("data_information", "on resume")
                    }

                    Lifecycle.Event.ON_PAUSE -> {
                        //Log.e("data_information", "on pause")
                    }

                    Lifecycle.Event.ON_STOP -> {
                        //Log.e("data_information", "on stop")
                    }

                    Lifecycle.Event.ON_DESTROY -> {
                        //Log.e("data_information", "on destroy")
                    }

                    Lifecycle.Event.ON_ANY -> {
                        //Log.e("data_information", "on any")
                    }
                }
            }

        })
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        //Log.d("data_information", "Call onActivityCreated")

    }

    override fun onActivityStarted(activity: Activity) {
        //Log.d("data_information", "Call onActivityStarted ${activity.toString()}")

    }

    override fun onActivityResumed(activity: Activity) {
        //Log.d("data_information", "Call onActivityResumed")

    }

    override fun onActivityPaused(activity: Activity) {
        //Log.d("data_information", "Call onActivityPaused")

    }

    override fun onActivityStopped(activity: Activity) {
        //Log.d("data_information", "Call onActivityStopped")

    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        //Log.d("data_information", "Call onActivitySaveInstanceState")

    }

    override fun onActivityDestroyed(activity: Activity) {
        //Log.d("data_information", "Call onActivityDestroyed")

    }

}