plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.app.loginregister"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.loginregister"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.lifecycle.process)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    //Constraint layout
    implementation(libs.androidx.constraintlayout.compose)

    //retrofit
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //coroutines
    implementation(libs.kotlinx.coroutines.android)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //Data store
    implementation(libs.androidx.datastore.preferences)

    //LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //Splash screen
    implementation(libs.androidx.core.splashscreen)

    //Glide
    implementation(libs.compose)

}