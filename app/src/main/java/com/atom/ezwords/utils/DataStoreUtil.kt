package com.atom.ezwords.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 *  author : liuxe
 *  date : 2023/12/27 11:32
 *  description :
 */
class DataStoreUtil(private val context: Context) {
    companion object {
        val Context.dataStore by preferencesDataStore("ezword")
        val SCORE = stringPreferencesKey("score")
    }
    val getScore = context.dataStore.data
        .map { preferences ->
            preferences[SCORE] ?: ""
        }
    suspend fun saveScore(value: String) {
        context.dataStore.edit { preferences ->
            preferences[SCORE] = value
        }
    }
}