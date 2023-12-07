package com.awesomejim.pokedex.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber


/**
 * Created by Awesome Jim on.
 * 07/12/2023
 */

internal class HttpRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder().url(originalRequest.url).build()
        Timber.d(request.toString())
        return chain.proceed(request)
    }
}