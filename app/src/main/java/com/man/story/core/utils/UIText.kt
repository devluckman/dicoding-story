package com.man.story.core.utils

import androidx.annotation.StringRes

/**
 *
 * Created by Lukmanul Hakim on  04/10/22
 * devs.lukman@gmail.com
 */
sealed class UIText {
    data class DynamicText(val value: String?) : UIText()
    data class StringResource(@StringRes val id: Int) : UIText()
}