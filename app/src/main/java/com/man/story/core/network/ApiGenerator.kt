package com.man.story.core.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * Created by Lukmanul Hakim on  28/09/22
 * devs.lukman@gmail.com
 */
class ApiGenerator(
    private val baseURL: String, // do implementation multiple base url
    private val client: OkHttpClient
) {
    fun <T> createService(clazz: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(clazz)
    }
}