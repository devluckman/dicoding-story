package com.man.story.presentation.stories

import android.content.Intent
import android.provider.Settings
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.man.story.R
import com.man.story.core.base.BaseActivity
import com.man.story.core.extentions.ifTrue
import com.man.story.core.extentions.observeData
import com.man.story.core.utils.popup.PopUpBuilder
import com.man.story.databinding.ActivityStoriesBinding
import com.man.story.databinding.ItemStoryBinding
import com.man.story.domain.model.story.StoryModel
import com.man.story.presentation.MainActivity
import com.man.story.presentation.addstory.AddStoryActivity
import com.man.story.presentation.maps.MapsActivity
import com.man.story.presentation.stories.adapter.LoadingAdapter
import com.man.story.presentation.stories.adapter.StoriesAdapter
import com.man.story.presentation.storydetail.StoryDetailActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * Created by Lukmanul Hakim on  04/10/22
 * devs.lukman@gmail.com
 */

@AndroidEntryPoint
class StoriesActivity : BaseActivity<ActivityStoriesBinding>(ActivityStoriesBinding::inflate) {

    val viewModel: StoriesViewModel by viewModels()
    private val storiesAdapter = StoriesAdapter(::onDetailStory)

    override fun ActivityStoriesBinding.initialBinding() {

        val loadStateAdapter = LoadingAdapter { storiesAdapter.retry() }

        rvStoriesList.adapter = storiesAdapter.withLoadStateFooter(loadStateAdapter)

        actionExplorer.setOnClickListener { explorer() }

        fabAddStory goTo AddStoryActivity::class.java

        observeData(viewModel.storiesList) { pagingData ->
            storiesAdapter.submitData(lifecycle, pagingData)
        }

        observeData(viewModel.isLogout) { state ->
            state.ifTrue {
                MainActivity::class.java.goWithFinish()
            }
        }
    }

    private fun onDetailStory(view: ItemStoryBinding, data: StoryModel) {
        val animationBundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(view.ivItemPhoto, getString(R.string.transition_iv_detail_photo)),
            Pair(view.tvItemName, getString(R.string.transition_tv_detail_name))
        ).toBundle()
        Intent(this, StoryDetailActivity::class.java).apply {
            putExtra(StoryDetailActivity.KEY_STORY, data)
            startActivity(this, animationBundle)
        }
    }

    private fun explorer() {
        PopUpBuilder.explorer(this, binding.actionExplorer, object : PopUpBuilder.ExplorerCallback {
            override fun onMaps() {
                MapsActivity::class.java.go()
            }

            override fun onLanguage() {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            override fun onLogout() {
                viewModel.logout()
            }
        })
    }
}