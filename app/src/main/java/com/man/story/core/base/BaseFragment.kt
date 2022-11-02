package com.man.story.core.base

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.man.story.R
import com.man.story.core.extentions.navigateTo
import com.man.story.core.utils.UIText


/**
 *
 * Created by Lukmanul Hakim on  27/09/22
 * devs.lukman@gmail.com
 */
abstract class BaseFragment<VM : ViewModel, VB : ViewBinding>(
    val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private lateinit var _binding: VB
    val binding get() = _binding
    abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.initialBinding()
    }

    open fun VB.initialBinding() {}

    private fun showMessage(message: String?) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }

    protected fun showMessage(uiText: UIText) {
        when(uiText){
            is UIText.DynamicText -> {
                showMessage(uiText.value)
            }
            is UIText.StringResource -> {
                showMessage(context?.getString(uiText.id))
            }
        }
    }

    protected infix fun View.goTo(destination: Int) {
        setOnClickListener { navigateTo(destination) }
    }

    protected fun Class<*>.goWithFinish() {
        activity?.let { activity ->
            activity.startActivity(
                Intent(activity, this).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            activity.overridePendingTransition(
                R.anim.transition_fade_in, R.anim.transition_fade_out
            )
            activity.finish()
        }
    }

    protected fun checkPermission(permission: String, granted: (Boolean) -> Unit) {
        activity?.let {
            granted.invoke(
                ContextCompat.checkSelfPermission(
                    it,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            )
        }
    }
}