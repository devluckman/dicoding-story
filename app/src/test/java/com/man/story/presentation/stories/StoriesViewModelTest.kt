package com.man.story.presentation.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.google.common.truth.Truth
import com.man.story.data.FakeApi
import com.man.story.data.FakeDataPreferences
import com.man.story.data.paging.StoriesPagingSource
import com.man.story.data.remote.Api
import com.man.story.data.repository.RepositoryImpl
import com.man.story.data.response.stories.StoriesResponseDTO.Companion.toDomain
import com.man.story.domain.model.story.StoryModel
import com.man.story.dummy.DataDummy
import com.man.story.utils.MainCoroutineRule
import com.man.story.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 *
 * Created by Lukmanul Hakim on  13/10/22
 * devs.lukman@gmail.com
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoriesViewModelTest {

    // region Initialize

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRule()

    private lateinit var api: Api
    private lateinit var dataPreferences: FakeDataPreferences
    private lateinit var pagingSource: StoriesPagingSource

    @Mock
    private lateinit var repositoryImpl: RepositoryImpl

    @Before
    fun setUp() {
        api = FakeApi(isSuccess = true)
        dataPreferences = FakeDataPreferences(tokenPref = "")
        pagingSource = StoriesPagingSource(api, dataPreferences)
    }

    // endregion

    // region Test Case login

    @Test
    fun `when Get Stories Data Success`() = runBlocking {
        val dummyStories = DataDummy.generateStoriesResponse().listStory.toDomain()
        val data: PagingData<StoryModel> = StoryPagingSource.snapshot(dummyStories)
        val expectedStories = MutableLiveData<PagingData<StoryModel>>()
        expectedStories.value = data
        Mockito.`when`(repositoryImpl.getStories()).thenReturn(expectedStories)

        val storiesViewModel = StoriesViewModel(repositoryImpl)
        val actualStories: PagingData<StoryModel> = storiesViewModel.storiesList.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryModel.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStories)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories, differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories[0].description, differ.snapshot()[0]?.description)
    }

    @Test
    fun `when Logout`() {
        val repositoryImpl = RepositoryImpl(api, dataPreferences, pagingSource)
        val storiesViewModel = StoriesViewModel(repositoryImpl)
        storiesViewModel.logout()
        Truth.assertThat(dataPreferences.tokenPref).isEmpty() // clear token
        Truth.assertThat(storiesViewModel.isLogout.getOrAwaitValue()).isTrue() // isLogout true
    }

    // endregion
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryModel>>>() {
    companion object {
        fun snapshot(items: List<StoryModel>): PagingData<StoryModel> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryModel>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryModel>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}