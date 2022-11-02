package com.man.story.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.man.story.core.base.Resource
import com.man.story.domain.model.login.LoginModel
import com.man.story.domain.model.story.StoryModel
import com.man.story.domain.request.AddStoryRequest
import com.man.story.domain.request.LoginRequest
import com.man.story.domain.request.RegisterRequest
import kotlinx.coroutines.flow.Flow

/**
 *
 * Created by Lukmanul Hakim on  28/09/22
 * devs.lukman@gmail.com
 */
interface Repository {

    suspend fun doLogin(request: LoginRequest): Flow<Resource<LoginModel>>

    suspend fun doRegister(request: RegisterRequest): Flow<Resource<String>>

    suspend fun doAddStory(request: AddStoryRequest): Flow<Resource<String>>

    suspend fun doLogout(): Flow<String>

    suspend fun getStoriesWithLocation() : Flow<Resource<List<StoryModel>>>

    fun getStories() : LiveData<PagingData<StoryModel>>
}