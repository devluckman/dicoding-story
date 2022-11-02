package com.man.story.presentation.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.man.story.core.base.Resource
import com.man.story.core.utils.UIText
import com.man.story.domain.model.story.StoryModel
import com.man.story.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Created by Lukmanul Hakim on  11/10/22
 * devs.lukman@gmail.com
 */
@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // region Initialize

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _resultStories = MutableLiveData<List<StoryModel>>()
    val resultStories: LiveData<List<StoryModel>> = _resultStories

    private val _errorMessage = MutableLiveData<UIText>()
    val errorMessage: LiveData<UIText> = _errorMessage

    // endregion

    fun getStoriesWithLocation() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getStoriesWithLocation().onEach { result ->
                when (result) {
                    is Resource.OnSuccess -> {
                        _isLoading.value = false
                        _resultStories.value = result.data
                    }
                    is Resource.OnError -> {
                        _isLoading.value = false
                        _errorMessage.value = result.uiText
                    }
                }
            }.launchIn(this)
        }
    }


}