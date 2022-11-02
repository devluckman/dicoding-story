package com.man.story.core.utils

import com.man.story.core.base.ApiConfig
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 *
 * Created by Lukmanul Hakim on  04/10/22
 * devs.lukman@gmail.com
 */
object OkHttpClientFactory {

    private const val DEFAULT_MAX_REQUEST = 30

    private const val TIMEOUT = 40

    fun create(
        apiConfig: ApiConfig,
    ): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .readTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

        if (apiConfig.buildDebug) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(interceptor).build()
        }

        val dispatcher = Dispatcher()
        dispatcher.maxRequests = DEFAULT_MAX_REQUEST
        builder.dispatcher(dispatcher)

        return builder.build()

    }
}