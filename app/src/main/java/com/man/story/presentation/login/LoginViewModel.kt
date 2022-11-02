package com.man.story.presentation.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.man.story.core.base.Resource
import com.man.story.core.utils.UIText
import com.man.story.domain.repository.Repository
import com.man.story.domain.request.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Created by Lukmanul Hakim on  27/09/22
 * devs.lukman@gmail.com
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // region Initialize

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _errorMessage = MutableLiveData<UIText>()
    val errorMessage: LiveData<UIText> = _errorMessage

    // endregion

    // region Event

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.doLogin(LoginRequest(email, password)).onEach { result ->
                when (result) {
                    is Resource.OnSuccess -> {
                        _isLoading.value = false
                        _isLoggedIn.value = true
                    }
                    is Resource.OnError -> {
                        _isLoading.value = false
                        _errorMessage.value = result.uiText
                    }
                }
            }.launchIn(this)
        }
    }

    // endregion

    // region Validation

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    val isSubmitEnabled: Flow<Boolean> = combine(_email, _password) { email, password ->
        val isEmailCorrect = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordCorrect = password.isNotEmpty() && password.length > 6
        return@combine isEmailCorrect and isPasswordCorrect
    }

    // endregion

}