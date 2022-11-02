package com.man.story.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.man.story.data.local.DataPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Created by Lukmanul Hakim on  27/09/22
 * devs.lukman@gmail.com
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dataPreferences: DataPreferences
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private var isLoginStateJob: Job? = null

    init {
        initializeLogin()
    }

    private fun initializeLogin() {
        isLoginStateJob?.cancel()
        isLoginStateJob = viewModelScope.launch {
            val token = dataPreferences.token.first()
            _isLoggedIn.value = token.isNotBlank()
        }
    }

}