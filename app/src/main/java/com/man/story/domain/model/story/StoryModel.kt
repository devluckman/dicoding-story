package com.man.story.domain.model.story

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

/**
 *
 * Created by Lukmanul Hakim on  30/09/22
 * devs.lukman@gmail.com
 */
@Parcelize
data class StoryModel(
    val id: String?,
    val photoUrl: String,
    val name: String,
    val description: String,
    val latitude: Double? = null,
    val longitude: Double? = null
) : Parcelable {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}