package com.man.story.core.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.man.story.R

/**
 *
 * Created by Lukmanul Hakim on  27/09/22
 * devs.lukman@gmail.com
 */
class PasswordEditText(context: Context, attributeSet: AttributeSet) :
    AppCompatEditText(context, attributeSet) {

    init {
        addTextChangedListener {
            it?.let { text ->
                if (text.isNotEmpty()){
                    this.error = if (text.length < MIN_LENGTH) {
                        context.getString(R.string.password_validation)
                    } else null
                }
            }
        }
    }

    companion object {
        const val MIN_LENGTH = 6
    }
}