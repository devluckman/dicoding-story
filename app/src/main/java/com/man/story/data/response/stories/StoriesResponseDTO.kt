package com.man.story.data.response.stories

import com.google.gson.annotations.SerializedName
import com.man.story.core.base.BaseResponse
import com.man.story.domain.model.story.StoryModel

class StoriesResponseDTO : BaseResponse() {

    @SerializedName("listStory")
    var listStory: List<StoryItem>? = null

    companion object {
        fun StoryItem.toDomain(): StoryModel = StoryModel(
            id = id.orEmpty(),
            name = name.orEmpty(),
            photoUrl = photoUrl.orEmpty(),
            description = description.orEmpty()
        )

        fun List<StoryItem>?.toDomain(): List<StoryModel> = this?.map {
            StoryModel(
                id = it.id.orEmpty(),
                name = it.name.orEmpty(),
                photoUrl = it.photoUrl.orEmpty(),
                description = it.description.orEmpty(),
                latitude = it.lat,
                longitude = it.lon
            )
        } ?: emptyList()
    }

}