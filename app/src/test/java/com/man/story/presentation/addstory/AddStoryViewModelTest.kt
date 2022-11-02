package com.man.story.presentation.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth
import com.man.story.core.utils.UIText
import com.man.story.data.FakeApi
import com.man.story.data.FakeDataPreferences
import com.man.story.data.paging.StoriesPagingSource
import com.man.story.data.remote.Api
import com.man.story.data.repository.RepositoryImpl
import com.man.story.dummy.FakeRequest
import com.man.story.utils.MainCoroutineRule
import com.man.story.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.io.File

/**
 *
 * Created by Lukmanul Hakim on  13/10/22
 * devs.lukman@gmail.com
 */
@ExperimentalCoroutinesApi
class AddStoryViewModelTest {

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
    fun `when Upload story success, isUploaded is true`() {
        val repositoryImpl = RepositoryImpl(api, dataPreferences, pagingSource)
        val addStoryViewModel = AddStoryViewModel(repositoryImpl)
        val file = Mockito.mock(File::class.java)
        addStoryViewModel.setLocation(LatLng(FakeRequest.LAT, FakeRequest.LON))
        addStoryViewModel.setDescription("description")
        addStoryViewModel.setFile(file)
        addStoryViewModel.uploadStory()
        Truth.assertThat(addStoryViewModel.isUploaded.getOrAwaitValue()).isTrue()
    }

    @Test
    fun `when Upload story failed`() {
        val expectedResult = UIText.DynamicText("Response.error()")
        val fakeApi = FakeApi(isSuccess = false)
        val repositoryImpl = RepositoryImpl(fakeApi, dataPreferences, pagingSource)
        val addStoryViewModel = AddStoryViewModel(repositoryImpl)
        val file = Mockito.mock(File::class.java)
        addStoryViewModel.setLocation(LatLng(FakeRequest.LAT, FakeRequest.LON))
        addStoryViewModel.setDescription("description")
        addStoryViewModel.setFile(file)
        addStoryViewModel.uploadStory()
        Truth.assertThat(addStoryViewModel.errorMessage.getOrAwaitValue()).isEqualTo(expectedResult)
    }
    // endregion

}