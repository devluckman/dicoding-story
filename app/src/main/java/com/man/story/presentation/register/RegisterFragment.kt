package com.man.story.presentation.register

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
import com.man.story.core.extentions.onBack
import com.man.story.databinding.FragmentRegisterBinding
import com.man.story.domain.request.RegisterRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment :
    BaseFragment<RegisterViewModel, FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    override val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData(viewModel.isLoading) {
            binding.apply {
                edRegisterName.isEnabled = !it
                edRegisterEmail.isEnabled = !it
                edRegisterPassword.isEnabled = !it
                btnSubmit.isEnabled = !it
                btnSubmit.text = if (it) getString(R.string.registering) else getString(R.string.register)
                progressBar.isVisible = it
            }
        }

        observeData(viewModel.isRegistered) {
            it.ifTrue {
                onBack()
            }
        }

        observeData(viewModel.errorMessage) {
            showMessage(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSubmitEnabled.collect {
                binding.btnSubmit.isEnabled = it
            }
        }

        binding.edRegisterName.addTextChangedListener {
            viewModel.setName(it.toString())
        }

        binding.edRegisterEmail.addTextChangedListener {
            viewModel.setEmail(it.toString())
        }

        binding.edRegisterPassword.addTextChangedListener {
            viewModel.setPassword(it.toString())
        }

        binding.btnSubmit.setOnClickListener {
            viewModel.register(
                RegisterRequest(
                    name = binding.edRegisterName.getString,
                    email = binding.edRegisterEmail.getString,
                    password = binding.edRegisterPassword.getString,
                )
            )
        }
    }

}