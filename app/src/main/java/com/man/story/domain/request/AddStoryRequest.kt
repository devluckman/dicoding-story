package com.man.story.domain.request

import java.io.File

/**
 *
 * Created by Lukmanul Hakim on  04/10/22
 * devs.lukman@gmail.com
 */
data class AddStoryRequest(
    val description: String,
    val photo: File,
    val lat: Double? = null,
    val lon: Double? = null
)