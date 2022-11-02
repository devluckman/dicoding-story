package com.man.story.presentation.storydetail

import com.bumptech.glide.Glide
import com.man.story.core.base.BaseActivity
import com.man.story.databinding.ActivityStoryDetailBinding
import com.man.story.domain.model.story.StoryModel

class StoryDetailActivity : BaseActivity<ActivityStoryDetailBinding>(
    ActivityStoryDetailBinding::inflate
) {

    override fun ActivityStoryDetailBinding.initialBinding() {
        val data = intent.getParcelableExtra<StoryModel>(KEY_STORY)
        data?.let {
            Glide.with(this@StoryDetailActivity)
                .load(data.photoUrl)
                .into(ivDetailPhoto)
            tvDetailName.text = data.name
            tvDetailDescription.text = data.description
        }
    }

    companion object {
        const val KEY_STORY = "KEY_STORY"
    }
}