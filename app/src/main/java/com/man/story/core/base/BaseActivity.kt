package com.man.story.core.base

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.man.story.R
import com.man.story.core.utils.UIText

/**
 *
 * Created by Lukmanul Hakim on  27/09/22
 * devs.lukman@gmail.com
 */
abstract class BaseActivity<VB : ViewBinding>(
    private val inflate: (LayoutInflater) -> VB
) : AppCompatActivity() {

    protected val binding: VB by lazy {
        inflate.invoke(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.initialBinding()
    }

    open fun VB.initialBinding() {}

    protected fun checkPermission(permission: String, granted: (Boolean) -> Unit) {
        granted.invoke(
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    protected fun checkPermission(permission: String): Boolean = ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED

    private fun showMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    protected fun showMessage(uiText: UIText) {
        when (uiText) {
            is UIText.DynamicText -> {
                showMessage(uiText.value)
            }
            is UIText.StringResource -> {
                showMessage(getString(uiText.id))
            }
        }
    }

    protected infix fun View.goTo(destination: Class<*>) {
        setOnClickListener {
            startActivity(
                Intent(this@BaseActivity, destination)
            )
            overridePendingTransition(
                R.anim.transition_fade_in, R.anim.transition_fade_out
            )
        }
    }


    protected fun Class<*>.goWithFinish() {
        startActivity(
            Intent(this@BaseActivity, this).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
        overridePendingTransition(
            R.anim.transition_fade_in, R.anim.transition_fade_out
        )
        finish()
    }

    protected fun Class<*>.go() {
        startActivity(
            Intent(this@BaseActivity, this)
        )
        overridePendingTransition(
            R.anim.transition_fade_in, R.anim.transition_fade_out
        )
    }
}