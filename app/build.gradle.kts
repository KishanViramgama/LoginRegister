plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.app.loginregister"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.app.loginregister"
        minSdk = 23
        targetSdk = 33
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
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

object LibVersion {
    const val compose_version = "1.4.3"
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:${LibVersion.compose_version}")
    implementation("androidx.compose.ui:ui-tooling-preview:${LibVersion.compose_version}")
    implementation("androidx.compose.material3:material3:1.2.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-process:2.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${LibVersion.compose_version}")
    debugImplementation("androidx.compose.ui:ui-tooling:${LibVersion.compose_version}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${LibVersion.compose_version}")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")

    //Constraint layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //retrofit
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    //hilt
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    //Data store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    //Splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    //Glide
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")

}