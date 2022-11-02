package com.man.story.core.base

import com.man.story.core.utils.UIText

/**
 *
 * Created by Lukmanul Hakim on  28/09/22
 * devs.lukman@gmail.com
 */
sealed class Resource<T> {
    data class OnSuccess<T>(val data: T?) : Resource<T>()
    data class OnError<T>(val uiText: UIText) : Resource<T>()
}