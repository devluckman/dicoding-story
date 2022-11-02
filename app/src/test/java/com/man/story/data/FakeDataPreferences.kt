package com.man.story.data

import com.man.story.data.local.DataPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 *
 * Created by Lukmanul Hakim on  12/10/22
 * devs.lukman@gmail.com
 */
class FakeDataPreferences(
    var tokenPref : String? = null
) : DataPreferences {

    override val token: Flow<String> = flow {
        tokenPref?.let { emit(it) }
    }

    override suspend fun saveToken(token: String) {
        tokenPref = token
    }
}