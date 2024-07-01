package com.kt.apps.media.core.di

import com.google.gson.Gson
import com.kt.apps.media.core.BuildConfig
import com.kt.apps.media.core.api.GithubAPI
import com.kt.apps.media.core.di.qualifiers.CoroutineDispatcherType
import com.kt.apps.media.core.di.qualifiers.CoroutineScopeQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.internal.Provider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val LOGGING_INTERCEPTOR = "named:logging"
    const val TIMEOUT_DEFAULT = 60_000L

    @Provides
    @Named(LOGGING_INTERCEPTOR)
    fun provideInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            this.level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        @Named(LOGGING_INTERCEPTOR)
        interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_DEFAULT, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT_DEFAULT, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create(Gson())
    }

    @Provides
    fun provideApi(
        client: OkHttpClient,
        converter: Converter.Factory
    ): GithubAPI {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(converter)
            .baseUrl(GithubAPI.BASE_URL)
            .build()
            .create(GithubAPI::class.java)
    }


    @Provides
    @CoroutineScopeQualifier(CoroutineDispatcherType.IO)
    fun providesDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @CoroutineScopeQualifier(CoroutineDispatcherType.DEFAULT)
    fun providesDispatcherDefault(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @CoroutineScopeQualifier(CoroutineDispatcherType.MAIN)
    fun providesDispatcherMain(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @CoroutineScopeQualifier(CoroutineDispatcherType.IO)
    fun provideIOCoroutineScope(): Provider<CoroutineScope> {
        return Provider<CoroutineScope> {
            CoroutineScope(Dispatchers.IO)
        }
    }
}