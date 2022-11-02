package com.man.story.presentation.addstory

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.man.story.core.base.Resource
import com.man.story.core.utils.UIText
import com.man.story.domain.repository.Repository
import com.man.story.domain.request.AddStoryRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 *
 * Created by Lukmanul Hakim on  03/10/22
 * devs.lukman@gmail.com
 */
@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // region Initialize

    // region Initialize

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isUploaded = MutableLiveData<Boolean>()
    val isUploaded: LiveData<Boolean> = _isUploaded

    private val _errorMessage = MutableLiveData<UIText>()
    val errorMessage: LiveData<UIText> = _errorMessage

    // endregion

    private val _description = MutableStateFlow("")
    private val _file = MutableStateFlow<File?>(null)
    private val _location = MutableLiveData<LatLng>()

    fun setDescription(description: String) {
        _description.value = description
    }

    fun setFile(file: File) {
        _file.value = file
    }

    fun setLocation(location: LatLng?) {
        _location.value = location
    }

    val isSubmitEnabled: Flow<Boolean> = combine(_file, _description) { file, description ->
        return@combine file != null && description.isNotBlank()
    }

    // endregion

    // region Upload

    fun uploadStory() {
        val request = _location.value?.let {
            AddStoryRequest(
                description = _description.value,
                photo = _file.value!!,
                lat = it.latitude,
                lon = it.longitude
            )
        } ?: AddStoryRequest(
            description = _description.value,
            photo = _file.value!!
        )

        _isLoading.value = true
        viewModelScope.launch {
            repository.doAddStory(request).onEach {
                when (it) {
                    is Resource.OnSuccess -> {
                        _isLoading.value = false
                        _isUploaded.value = true
                    }
                    is Resource.OnError -> {
                        _isLoading.value = false
                        _errorMessage.value = it.uiText
                    }
                }
            }.launchIn(this)
        }
    }


    // endregion

}