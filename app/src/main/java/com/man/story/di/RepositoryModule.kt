package com.man.story.di

import com.man.story.data.local.DataPreferences
import com.man.story.data.paging.StoriesPagingSource
import com.man.story.data.remote.Api
import com.man.story.data.repository.RepositoryImpl
import com.man.story.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 *
 * Created by Lukmanul Hakim on  28/09/22
 * devs.lukman@gmail.com
 */
@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun providePaging(
        api: Api,
        dataPreferences: DataPreferences
    ): StoriesPagingSource {
        return StoriesPagingSource(
            api = api,
            dataStore = dataPreferences
        )
    }

    @Provides
    fun provideRepository(
        api: Api,
        dataPreferences: DataPreferences,
        pagingSource: StoriesPagingSource
    ): Repository {
        return RepositoryImpl(
            api = api,
            dataStore = dataPreferences,
            pagingSource = pagingSource
        )
    }

}