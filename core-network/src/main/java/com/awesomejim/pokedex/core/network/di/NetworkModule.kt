package com.awesomejim.pokedex.core.network.di

import com.awesomejim.pokedex.core.network.BuildConfig
import com.awesomejim.pokedex.core.network.service.PokedexClient
import com.awesomejim.pokedex.core.network.service.PokedexService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Created by Awesome Jim on.
 * 07/12/2023
 */

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Singleton
    @Provides
    fun providesJson(): Json = Json { ignoreUnknownKeys = true }


    @Singleton
    @Provides
    fun provideBaseUrl(): String = BuildConfig.POKE_API_BASE_URL 

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else HttpLoggingInterceptor.Level.NONE
        return HttpLoggingInterceptor().also {
            it.level = level
        }
    }

    @Singleton
    @Provides
    fun provideConnectionSpec(): ConnectionSpec {
        return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
            )
            .build()
    }

    /**
     *
     * @param spec ConnectionSpec
     * @return OkHttpClient
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(spec: ConnectionSpec, loggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectionSpecs(Collections.singletonList(spec))
            .addInterceptor(loggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseURL:  String,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providePokedexService(retrofit: Retrofit): PokedexService {
        return retrofit.create(PokedexService::class.java)
    }

    @Provides
    @Singleton
    fun providePokedexClient(pokedexService: PokedexService): PokedexClient {
        return PokedexClient(pokedexService)
    }
}