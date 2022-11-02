package com.man.story.presentation.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.man.story.core.utils.UIText
import com.man.story.data.FakeApi
import com.man.story.data.FakeDataPreferences
import com.man.story.data.paging.StoriesPagingSource
import com.man.story.data.remote.Api
import com.man.story.data.repository.RepositoryImpl
import com.man.story.data.response.stories.StoriesResponseDTO.Companion.toDomain
import com.man.story.domain.repository.Repository
import com.man.story.dummy.DataDummy
import com.man.story.dummy.FakeRequest
import com.man.story.presentation.login.LoginViewModel
import com.man.story.utils.MainCoroutineRule
import com.man.story.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 *
 * Created by Lukmanul Hakim on  12/10/22
 * devs.lukman@gmail.com
 */
@ExperimentalCoroutinesApi
class MapsViewModelTest {

    // region Initialize

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    private lateinit var api: Api
    private lateinit var dataPreferences: FakeDataPreferences
    private lateinit var pagingSource: StoriesPagingSource

    @Before
    fun setUp() {
        api = FakeApi(isSuccess = true)
        dataPreferences = FakeDataPreferences(tokenPref = "")
        pagingSource = StoriesPagingSource(api, dataPreferences)
    }

    // endregion

    // region Test Case login

    @Test
    fun `when Success getStoriesList`() {
        val expectedResult = DataDummy.generateStoriesResponse().listStory.toDomain()
        val repositoryImpl = RepositoryImpl(api, dataPreferences, pagingSource)
        val mapsViewModel = MapsViewModel(repositoryImpl)
        mapsViewModel.getStoriesWithLocation()
        Truth.assertThat(mapsViewModel.resultStories.getOrAwaitValue()).isNotEmpty()
        Truth.assertThat(mapsViewModel.resultStories.getOrAwaitValue()).isEqualTo(expectedResult)
    }

    @Test
    fun `when Failed getStoriesList`() {
        val expectedResult = UIText.DynamicText("Response.error()")
        val fakeApi = FakeApi(isSuccess = false)
        val repositoryImpl = RepositoryImpl(fakeApi, dataPreferences, pagingSource)
        val loginViewModel = LoginViewModel(repositoryImpl)
        loginViewModel.login(
            email = FakeRequest.EMAIL,
            password = FakeRequest.PASSWORD
        )
        Truth.assertThat(loginViewModel.errorMessage.getOrAwaitValue()).isEqualTo(expectedResult)
    }

    // endregion

}