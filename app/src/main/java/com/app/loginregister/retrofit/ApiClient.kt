package com.app.loginregister.retrofit

import android.app.Application
import android.content.Context
import com.app.loginregister.util.BasePath
import com.app.loginregister.util.Method
import com.app.loginregister.util.MyDataStore
import com.app.loginregister.util.ResponseCodeCheck
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiClient {

    @Provides
    @Singleton
    fun providerRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BasePath.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun providesResponseCodeCheck(): ResponseCodeCheck {
        return ResponseCodeCheck()
    }

    @Provides
    @Singleton
    fun provideMethod(context: Context): Method {
        return Method(context)
    }

    @Provides
    @Singleton
    fun provideMyDataStore(context: Context): MyDataStore {
        return MyDataStore(context)
    }

}