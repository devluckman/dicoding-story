package com.man.story.presentation.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.man.story.R
import com.man.story.core.base.BaseFragment
import com.man.story.core.extentions.getString
import com.man.story.core.extentions.ifTrue
import com.man.story.core.extentions.observeData
import com.man.story.core.extentions.whatIf
import com.man.story.databinding.FragmentLoginBinding
import com.man.story.presentation.stories.StoriesActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData(viewModel.isLoggedIn) {
            it.ifTrue {
                StoriesActivity::class.java.goWithFinish()
            }
        }

        observeData(viewModel.isLoading) {
            binding.edLoginEmail.isEnabled = !it
            binding.edLoginPassword.isEnabled = !it
            binding.btnLogin.isEnabled = !it
            binding.btnLogin.text =
                if (it) getString(R.string.logging_in) else getString(R.string.login)
            binding.progressBar.isVisible = it
        }

        observeData(viewModel.errorMessage) {
            showMessage(it)
        }

        binding.edLoginEmail.addTextChangedListener {
            viewModel.setEmail(it.toString())
        }

        binding.edLoginPassword.addTextChangedListener {
            viewModel.setPassword(it.toString())
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                email = binding.edLoginEmail.getString,
                password = binding.edLoginPassword.getString
            )
        }

        binding.btnRegister goTo R.id.action_loginFragment_to_registerFragment

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSubmitEnabled.collect {
                binding.btnLogin.isEnabled = it
            }
        }
    }
}