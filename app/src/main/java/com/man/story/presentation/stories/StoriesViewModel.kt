package com.man.story.presentation.stories

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.man.story.domain.model.story.StoryModel
import com.man.story.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Created by Lukmanul Hakim on  28/09/22
 * devs.lukman@gmail.com
 */
@HiltViewModel
class StoriesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val storiesList: LiveData<PagingData<StoryModel>> =
        repository.getStories()
            .cachedIn(viewModelScope)

    private val _isLogout = MutableLiveData<Boolean>()
    val isLogout: LiveData<Boolean> = _isLogout

    private var logoutJob: Job? = null

    fun logout() {
        logoutJob?.cancel()
        logoutJob = viewModelScope.launch {
            repository.doLogout().collect { token ->
                _isLogout.value = token.isEmpty()
            }
        }
    }
}