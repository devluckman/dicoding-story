package com.man.story.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 *
 * Created by Lukmanul Hakim on  28/09/22
 * devs.lukman@gmail.com
 */
class DataPreferencesImpl(appContext: Context) : DataPreferences {

    private val dataStore: DataStore<Preferences> = appContext.dataStore

    override val token: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    companion object {

        private val TOKEN_KEY = stringPreferencesKey("token")

        private val Context.dataStore: DataStore<Preferences> by
        preferencesDataStore(name = "story_app")

    }


}