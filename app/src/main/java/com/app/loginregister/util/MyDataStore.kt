package com.app.loginregister.util

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MyDataStore constructor(private val context: Context) {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        private val ID_KEY = stringPreferencesKey("id")
        private val IS_LOGIN = booleanPreferencesKey("isLogin")
    }

    //Insert user id
    suspend fun insertUserID(id: String) {
        context.dataStore.edit { preferences ->
            preferences[ID_KEY] = id
        }
    }

    //Get user id
    val getUserID: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[ID_KEY] ?: ""
        }

    //User login or not save
    suspend fun isUserLogin(isLogin: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGIN] = isLogin
        }
    }

    //Check user login or not
    val isUserLogin: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[IS_LOGIN] ?: false
        }

}