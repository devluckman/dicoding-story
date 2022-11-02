package com.man.story.core.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.man.story.R

/**
 *
 * Created by Lukmanul Hakim on  27/09/22
 * devs.lukman@gmail.com
 */
class EmailEditText(context: Context, attributeSet: AttributeSet) :
    AppCompatEditText(context, attributeSet) {

    init {
        addTextChangedListener {
            it?.let { text ->
                if (text.isNotEmpty()){
                    this.error = if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                        context.getString(R.string.email_not_valid)
                    } else null
                }
            }
        }
    }

}