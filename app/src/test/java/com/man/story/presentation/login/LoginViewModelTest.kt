package com.man.story.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.man.story.core.base.Resource
import com.man.story.core.utils.UIText
import com.man.story.data.FakeApi
import com.man.story.data.FakeDataPreferences
import com.man.story.data.paging.StoriesPagingSource
import com.man.story.data.remote.Api
import com.man.story.data.repository.RepositoryImpl
import com.man.story.domain.model.login.LoginModel
import com.man.story.domain.request.LoginRequest
import com.man.story.dummy.DataDummy
import com.man.story.dummy.FakeRequest
import com.man.story.utils.MainCoroutineRule
import com.man.story.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

/**
 *
 * Created by Lukmanul Hakim on  12/10/22
 * devs.lukman@gmail.com
 */
@ExperimentalCoroutinesApi
class LoginViewModelTest {

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
    fun `when Login Success, isLoggedIn is true`() {
        val repositoryImpl = RepositoryImpl(api, dataPreferences, pagingSource)
        val loginViewModel = LoginViewModel(repositoryImpl)
        loginViewModel.login(
            email = FakeRequest.EMAIL,
            password = FakeRequest.PASSWORD
        )
        assertThat(loginViewModel.isLoggedIn.getOrAwaitValue()).isTrue()
    }

    @Test
    fun `when Login Failed`() {
        val expectedResult = UIText.DynamicText("Response.error()")
        val fakeApi = FakeApi(isSuccess = false)
        val repositoryImpl = RepositoryImpl(fakeApi, dataPreferences, pagingSource)
        val loginViewModel = LoginViewModel(repositoryImpl)
        loginViewModel.login(
            email = FakeRequest.EMAIL,
            password = FakeRequest.PASSWORD
        )
        assertThat(loginViewModel.errorMessage.getOrAwaitValue()).isEqualTo(expectedResult)
    }
    // endregion
}