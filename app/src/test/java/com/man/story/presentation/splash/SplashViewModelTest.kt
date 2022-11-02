package com.man.story.presentation.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.man.story.data.FakeDataPreferences
import com.man.story.data.local.DataPreferencesImpl
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
class SplashViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    
    @Test
    fun `when User Already login, isLoggedIn is true `() {
        val fakePreferences = FakeDataPreferences(tokenPref = "this_is_token_dummy")
        val splashViewModel = SplashViewModel(fakePreferences)
        assertThat(splashViewModel.isLoggedIn.getOrAwaitValue()).isTrue()
    }

    @Test
    fun `when User not ready login, isLoggedIn is false`() {
        val fakePreferences = FakeDataPreferences(tokenPref = "")
        val splashViewModel = SplashViewModel(fakePreferences)
        assertThat(splashViewModel.isLoggedIn.getOrAwaitValue()).isFalse()
    }

}