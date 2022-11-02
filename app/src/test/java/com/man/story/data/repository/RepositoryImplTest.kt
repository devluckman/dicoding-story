package com.man.story.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.man.story.core.base.Resource
import com.man.story.core.utils.UIText
import com.man.story.data.FakeApi
import com.man.story.data.FakeDataPreferences
import com.man.story.data.paging.StoriesPagingSource
import com.man.story.data.remote.Api
import com.man.story.data.response.stories.StoriesResponseDTO.Companion.toDomain
import com.man.story.domain.model.login.LoginModel
import com.man.story.domain.model.story.StoryModel
import com.man.story.domain.request.AddStoryRequest
import com.man.story.domain.request.LoginRequest
import com.man.story.domain.request.RegisterRequest
import com.man.story.dummy.DataDummy
import com.man.story.dummy.FakeRequest
import com.man.story.presentation.stories.StoryPagingSource
import com.man.story.presentation.stories.noopListUpdateCallback
import com.man.story.utils.MainCoroutineRule
import junit.framework.Assert.assertEquals
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
import java.io.File

/**
 *
 * Created by Lukmanul Hakim on  13/10/22
 * devs.lukman@gmail.com
 */
@ExperimentalCoroutinesApi
class RepositoryImplTest {

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
    fun `Successful login, save token and emits login model to presentation`() = runBlocking {
        val expectedLoginResult = DataDummy.generateLoginModel()
        val repositoryImpl = RepositoryImpl(api, dataPreferences, pagingSource)
        repositoryImpl.doLogin(
            LoginRequest(
                FakeRequest.EMAIL,
                FakeRequest.PASSWORD
            )
        ).test {
            awaitItem().let {
                assertThat(dataPreferences.tokenPref).isEqualTo(expectedLoginResult.token) // save token success
                assertThat(it).isInstanceOf(Resource.OnSuccess(expectedLoginResult)::class.java) // response success
            }
            awaitComplete()
        }
    }

    @Test
    fun `Failed login, emits error message from API to presentation`() = runBlocking {
        val apiFake = FakeApi(isSuccess = false)
        val repositoryImpl = RepositoryImpl(apiFake, dataPreferences, pagingSource)
        repositoryImpl.doLogin(
            LoginRequest(
                FakeRequest.EMAIL,
                FakeRequest.PASSWORD
            )
        ).test {
            awaitItem().let {
                val expectedUiText = UIText.DynamicText("failed")
                assertThat(it).isInstanceOf(Resource.OnError<LoginModel>(expectedUiText)::class.java)
            }
            awaitComplete()
        }
    }

    // endregion

    // region Test Case register

    @Test
    fun `Successful register, emits message success to presentation`() = runBlocking {
        val expectedLoginResult = DataDummy.generateRegisterResponse()
        val repositoryImpl = RepositoryImpl(api, dataPreferences, pagingSource)
        repositoryImpl.doRegister(
            RegisterRequest(
                email = FakeRequest.EMAIL,
                password = FakeRequest.PASSWORD,
                name = FakeRequest.NAME
            )
        ).test {
            awaitItem().let {
                assertThat(it).isInstanceOf(Resource.OnSuccess(expectedLoginResult.message)::class.java) // response success
            }
            awaitComplete()
        }
    }

    @Test
    fun `Failed register, emits error message from API to presentation`() = runBlocking {
        val apiFake = FakeApi(isSuccess = false)
        val repositoryImpl = RepositoryImpl(apiFake, dataPreferences, pagingSource)
        repositoryImpl.doRegister(
            RegisterRequest(
                email = FakeRequest.EMAIL,
                password = FakeRequest.PASSWORD,
                name = FakeRequest.NAME
            )
        ).test {
            awaitItem().let {
                val expectedUiText = UIText.DynamicText("failed")
                assertThat(it).isInstanceOf(Resource.OnError<LoginModel>(expectedUiText)::class.java)
            }
            awaitComplete()
        }
    }

    // endregion

    // region Test Case logout

    @Test
    fun `action logout clear token`() = runBlocking {
        val repositoryImpl = RepositoryImpl(api, dataPreferences, pagingSource)
        repositoryImpl.doLogout().test {
            awaitItem().let {
                assertThat(it).isEqualTo("") // token blank
            }
            awaitComplete()
        }
    }

    // endregion

    // region Test Case add story

    @Test
    fun `Successful Add story, emits message success to presentation`() = runBlocking {
        val expectedLoginResult = DataDummy.generateBaseResponse()
        val repositoryImpl = RepositoryImpl(api, dataPreferences, pagingSource)
        val file = Mockito.mock(File::class.java)
        repositoryImpl.doAddStory(
            AddStoryRequest(
                description = "description",
                photo = file,
                lat = 0.0,
                lon = 0.0
            )
        ).test {
            awaitItem().let {
                assertThat(it).isInstanceOf(Resource.OnSuccess(expectedLoginResult.message)::class.java) // response success
            }
            awaitComplete()
        }
    }

    @Test
    fun `Failed Add story, emits error message from API to presentation`() = runBlocking {
        val apiFake = FakeApi(isSuccess = false)
        val repositoryImpl = RepositoryImpl(apiFake, dataPreferences, pagingSource)
        val file = Mockito.mock(File::class.java)
        repositoryImpl.doAddStory(
            AddStoryRequest(
                description = "description",
                photo = file,
                lat = 0.0,
                lon = 0.0
            )
        ).test {
            awaitItem().let {
                val expectedUiText = UIText.DynamicText("failed")
                assertThat(it).isInstanceOf(Resource.OnError<LoginModel>(expectedUiText)::class.java)
            }
            awaitComplete()
        }
    }

    // endregion

    // region Test Case stories with location

    @Test
    fun `Successful Get Stories with location, emits data stories to presentation`() = runBlocking {
        val expectedLoginResult = DataDummy.generateStoriesResponse()
        val repositoryImpl = RepositoryImpl(api, dataPreferences, pagingSource)
        repositoryImpl.getStoriesWithLocation().test {
            awaitItem().let {
                assertThat(it).isNotNull()
                assertThat(it).isInstanceOf(Resource.OnSuccess(expectedLoginResult)::class.java) // response success
            }
            awaitComplete()
        }
    }

    @Test
    fun `Failed Get Stories with location, emits error message from API to presentation`() = runBlocking {
        val apiFake = FakeApi(isSuccess = false)
        val repositoryImpl = RepositoryImpl(apiFake, dataPreferences, pagingSource)
        repositoryImpl.getStoriesWithLocation().test {
            awaitItem().let {
                val expectedUiText = UIText.DynamicText("failed")
                assertThat(it).isInstanceOf(Resource.OnError<LoginModel>(expectedUiText)::class.java)
            }
            awaitComplete()
        }
    }

    // endregion

    // region Test Case stories pagination

    @Test
    fun `Success Get Stories Data`() = runBlocking {
        val expectedStory = DataDummy.getStories()
        val actualStory = api.getStories(
            bearerToken = FakeRequest.TOKEN,
            page = 1,
            size = 5
        )
        Assert.assertNotNull(actualStory)
        assertEquals(expectedStory.size, actualStory.body()?.listStory?.size)
    }

    // endregion





}