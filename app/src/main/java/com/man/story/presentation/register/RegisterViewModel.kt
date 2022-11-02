package com.man.story.presentation.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.man.story.core.base.Resource
import com.man.story.core.utils.UIText
import com.man.story.domain.repository.Repository
import com.man.story.domain.request.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Created by Lukmanul Hakim on  28/09/22
 * devs.lukman@gmail.com
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // region Initialize

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> = _isRegistered

    private val _errorMessage = MutableLiveData<UIText>()
    val errorMessage: LiveData<UIText> = _errorMessage

    // endregion

    // region Event

    fun register(request: RegisterRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.doRegister(request).onEach { result ->
                when (result) {
                    is Resource.OnSuccess -> {
                        _isLoading.value = false
                        _isRegistered.value = true
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

    private val _name = MutableStateFlow("")
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")

    fun setName(name : String) {
        _name.value = name
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    val isSubmitEnabled: Flow<Boolean> = combine(_name,_email, _password) { name, email, password ->
        val isEmailCorrect = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordCorrect = password.isNotEmpty() && password.length > 6
        return@combine isEmailCorrect and isPasswordCorrect and name.isNotEmpty()
    }

    // endregion
}