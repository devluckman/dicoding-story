package com.man.story.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.man.story.core.base.Resource
import com.man.story.core.extentions.asFlowStateEvent
import com.man.story.core.extentions.asFlowStateEventWithAction
import com.man.story.data.local.DataPreferences
import com.man.story.data.paging.StoriesPagingSource
import com.man.story.data.remote.Api
import com.man.story.data.response.login.LoginResponseDTO.Companion.toDomain
import com.man.story.data.response.stories.StoriesResponseDTO.Companion.toDomain
import com.man.story.domain.model.login.LoginModel
import com.man.story.domain.model.story.StoryModel
import com.man.story.domain.repository.Repository
import com.man.story.domain.request.AddStoryRequest
import com.man.story.domain.request.LoginRequest
import com.man.story.domain.request.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 *
 * Created by Lukmanul Hakim on  28/09/22
 * devs.lukman@gmail.com
 */
class RepositoryImpl(
    val api: Api,
    val dataStore: DataPreferences,
    val pagingSource: StoriesPagingSource
) : Repository {

    override suspend fun doLogin(request: LoginRequest): Flow<Resource<LoginModel>> = api.login(
        email = request.email,
        password = request.password
    ).asFlowStateEventWithAction(
        mapper = { it.toDomain() },
        action = { dataStore.saveToken(it.loginResult?.token.orEmpty()) }
    )

    override suspend fun doRegister(request: RegisterRequest): Flow<Resource<String>> =
        api.register(
            name = request.name,
            email = request.email,
            password = request.password
        ).asFlowStateEvent(
            mapper = { it.message ?: "User Created" }
        )

    override suspend fun doAddStory(request: AddStoryRequest): Flow<Resource<String>> {
        val bearerToken = "Bearer ${dataStore.token.first()}"
        val rbDescription = request.description.toRequestBody("text/plain".toMediaType())
        val photoMultipart = MultipartBody.Part.createFormData(
            name = "photo",
            filename = request.photo.name,
            body = try {
                compressImage(request.photo)
            } catch (e: Exception) {
                request.photo
            }.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )
        return api.addStory(
            bearerToken = bearerToken,
            description = rbDescription,
            photo = photoMultipart
        ).asFlowStateEvent(
            mapper = { it.message ?: "Upload Story Success" }
        )
    }

    override suspend fun doLogout(): Flow<String> {
        dataStore.saveToken("")
        return dataStore.token
    }

    override suspend fun getStoriesWithLocation(): Flow<Resource<List<StoryModel>>> {
        val bearerToken = "Bearer ${dataStore.token.first()}"
        return api.getStoriesWithLocation(
            bearerToken = bearerToken
        ).asFlowStateEvent(
            mapper = { it.listStory.toDomain() }
        )
    }

    override fun getStories(): LiveData<PagingData<StoryModel>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { pagingSource }
        ).liveData
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun compressImage(file: File): File = withContext(Dispatchers.IO) {
        val bitmap = BitmapFactory.decodeFile(file.path)

        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return@withContext file
    }
}