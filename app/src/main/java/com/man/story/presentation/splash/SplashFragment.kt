package com.man.story.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.man.story.R
import com.man.story.core.base.BaseFragment
import com.man.story.core.extentions.navigateTo
import com.man.story.core.extentions.observeData
import com.man.story.databinding.FragmentSplashBinding
import com.man.story.presentation.stories.StoriesActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>(
    FragmentSplashBinding::inflate
) {

    override val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData(viewModel.isLoggedIn) {
            when (it) {
                true -> {
                    StoriesActivity::class.java.goWithFinish()
                }
                false -> {
                    navigateTo(R.id.action_splashFragment_to_loginFragment)
                }
            }
        }
    }

}