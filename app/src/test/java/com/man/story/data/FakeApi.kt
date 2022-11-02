package com.man.story.data

import com.man.story.core.base.BaseResponse
import com.man.story.data.remote.Api
import com.man.story.data.response.login.LoginResponseDTO
import com.man.story.data.response.stories.StoriesResponseDTO
import com.man.story.dummy.DataDummy
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

/**
 *
 * Created by Lukmanul Hakim on  12/10/22
 * devs.lukman@gmail.com
 */
class FakeApi(
    var isSuccess: Boolean = true
) : Api {

    override suspend fun login(email: String, password: String): Response<LoginResponseDTO> {
        return if (isSuccess) Response.success(DataDummy.generateLoginResponse()) else Response.error(
            400,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Response<BaseResponse> {
        return if (isSuccess) Response.success(DataDummy.generateBaseResponse()) else Response.error(
            400,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
    }

    override suspend fun getStories(
        bearerToken: String,
        page: Int,
        size: Int
    ): Response<StoriesResponseDTO> {
        return if (isSuccess) Response.success(DataDummy.generateStoriesResponse()) else Response.error(
            400,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
    }

    override suspend fun getStoriesWithLocation(
        bearerToken: String,
        location: Int
    ): Response<StoriesResponseDTO> {
        return if (isSuccess) Response.success(DataDummy.generateStoriesResponse()) else Response.error(
            400,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
    }

    override suspend fun addStory(
        bearerToken: String,
        photo: MultipartBody.Part,
        description: RequestBody
    ): Response<BaseResponse> {
        return if (isSuccess) Response.success(DataDummy.generateBaseResponse()) else Response.error(
            400,
            "{\"key\":[\"somestuff\"]}"
                .toResponseBody("application/json".toMediaTypeOrNull())
        )
    }
}