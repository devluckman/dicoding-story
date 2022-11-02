package com.man.story.data.response.stories

import com.google.gson.annotations.SerializedName

/**
 *
 * Created by Lukmanul Hakim on  01/10/22
 * devs.lukman@gmail.com
 */
data class StoryItem(

    @SerializedName("photoUrl")
    val photoUrl: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("lon")
    val lon: Double? = null,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("lat")
    val lat: Double? = null
)
