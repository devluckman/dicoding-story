package com.man.story.di

import android.content.Context
import com.man.story.BuildConfig
import com.man.story.core.base.ApiConfig
import com.man.story.core.network.ApiGenerator
import com.man.story.core.utils.OkHttpClientFactory
import com.man.story.data.local.DataPreferences
import com.man.story.data.local.DataPreferencesImpl
import com.man.story.data.remote.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 *
 * Created by Lukmanul Hakim on  28/09/22
 * devs.lukman@gmail.com
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideApiConfig(): ApiConfig {
        return ApiConfig(
            baseUrl = BuildConfig.BASE_URL,
            buildDebug = BuildConfig.DEBUG
        )
    }

    @Provides
    fun provideDataPreferences(
        @ApplicationContext appContext: Context
    ): DataPreferences {
        return DataPreferencesImpl(appContext)
    }

    @Provides
    fun provideApi(
        apiConfig: ApiConfig,
    ): Api {
        return ApiGenerator(
            baseURL = apiConfig.baseUrl,
            client = OkHttpClientFactory.create(apiConfig)
        ).createService(Api::class.java)
    }

}