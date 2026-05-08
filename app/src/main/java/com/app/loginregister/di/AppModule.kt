package com.app.loginregister.di

import android.app.Application
import android.content.Context
import com.app.loginregister.network.domain.ApiInterface
import com.app.loginregister.network.utility.BasePath
import com.app.loginregister.network.utility.Header.KTOR_SAMPLE
import com.app.loginregister.network.utility.Header.X_API_KEY
import com.app.loginregister.util.Method
import com.app.loginregister.util.MyDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideKtorClient(json: Json): HttpClient {
        return HttpClient(OkHttp) {
            install(WebSockets)
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    @Provides
    @Singleton
    fun providerRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .addInterceptor {
                val request: Request = it.request().newBuilder()
                    .addHeader(X_API_KEY, KTOR_SAMPLE)
                    .build()
                it.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BasePath.BASE_URL)
            .client(client)
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
    fun provideMethod(context: Context): Method {
        return Method(context)
    }

    @Provides
    @Singleton
    fun provideMyDataStore(context: Context): MyDataStore {
        return MyDataStore(context)
    }

}