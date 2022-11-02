package com.man.story.data.remote

import com.man.story.core.base.BaseResponse
import com.man.story.data.response.login.LoginResponseDTO
import com.man.story.data.response.stories.StoriesResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 *
 * Created by Lukmanul Hakim on  28/09/22
 * devs.lukman@gmail.com
 */
interface Api {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponseDTO>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<BaseResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") bearerToken: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<StoriesResponseDTO>

    // 1 for get all stories with location
    // 0 for all stories without considering location
    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") bearerToken: String,
        @Query("location") location: Int = 1
    ): Response<StoriesResponseDTO>

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") bearerToken: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<BaseResponse>

}