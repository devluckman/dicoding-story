package com.man.story.core.base

import com.google.gson.annotations.SerializedName


open class BaseResponse(
    @field:SerializedName("error")
    var error: Boolean? = null,

    @field:SerializedName("message")
    var message: String? = null
)
