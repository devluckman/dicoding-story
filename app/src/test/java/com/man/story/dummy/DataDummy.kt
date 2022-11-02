package com.man.story.dummy

import com.man.story.core.base.BaseResponse
import com.man.story.data.response.login.LoginResponseDTO
import com.man.story.data.response.login.LoginResult
import com.man.story.data.response.stories.StoriesResponseDTO
import com.man.story.data.response.stories.StoryItem
import com.man.story.domain.model.login.LoginModel
import com.man.story.domain.model.story.StoryModel

/**
 *
 * Created by Lukmanul Hakim on  12/10/22
 * devs.lukman@gmail.com
 */
object DataDummy {

    fun getStories(): List<StoryModel> = listOf(
        StoryModel(
            id = "story-FvU4u0Vp2S3PMsFg",
            name = "Dimas",
            description = "Lorem Ipsum",
            latitude = -10.212,
            longitude = -16.002,
            photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png"
        ),
        StoryModel(
            id = "story-FvU4u0Vp2S3PMsFg",
            name = "Dimas 1",
            description = "Lorem Ipsum",
            latitude = -10.212,
            longitude = -16.002,
            photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png"
        ),
        StoryModel(
            id = "story-FvU4u0Vp2S3PMsFg",
            name = "Dimas 2",
            description = "Lorem Ipsum",
            latitude = -10.212,
            longitude = -16.002,
            photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png"
        )
    )

    fun generateLoginResponse() = LoginResponseDTO(
        loginResult = LoginResult(
            userId = "user-yj5pc_LARC_AgK61",
            name = "Arif Faizin",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
        )
    ).apply {
        error = false
        message = "success"
    }

    fun generateLoginModel() = LoginModel(
        userId = "user-yj5pc_LARC_AgK61",
        name = "Arif Faizin",
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
    )

    fun generateStoriesResponse() = StoriesResponseDTO().apply {
        listStory = listOf(
            StoryItem(
                id = "story-FvU4u0Vp2S3PMsFg",
                name = "Dimas",
                description = "Lorem Ipsum",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                lat = -10.212,
                lon = -16.002
            ),
            StoryItem(
                id = "story-FvU4u0Vp2S3PMsFg",
                name = "Reza Arap",
                description = "Lorem Ipsum",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                lat = -10.212,
                lon = -16.002
            ),
            StoryItem(
                id = "story-FvU4u0Vp2S3PMsFg",
                name = "Rizky Billar",
                description = "Lorem Ipsum",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                lat = -10.212,
                lon = -16.002
            )
        )
        error = false
        message = "success"
    }

    fun generateBaseResponse() = BaseResponse(
        error = false,
        message = "success"
    )

    fun generateRegisterResponse() = BaseResponse(
        error = false,
        message = "User Created"
    )

}