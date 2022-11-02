package com.man.story.data.local

import kotlinx.coroutines.flow.Flow

/**
 *
 * Created by Lukmanul Hakim on  12/10/22
 * devs.lukman@gmail.com
 */
interface DataPreferences {

    val token: Flow<String>

    suspend fun saveToken(token: String)
}