package com.example.secondhand.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserLoginTokenManager(context: Context) {
    private val dataStore: DataStore<Preferences> = context.createDataStore("login-prefs")

    companion object {
        val EMAIL = preferencesKey<String>("EMAIL")
        val NAME = preferencesKey<String>("NAME")
        val ACCESS_TOKEN = preferencesKey<String>("ACCESS_TOKEN")
        val BOOLEAN = preferencesKey<Boolean>("BOOLEAN")
    }

    suspend fun saveToken(
        email: String,
        name: String,
        accessToken: String
    ) {
        dataStore.edit {
            it[EMAIL] = email
            it[NAME] = name
            it[ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun setBoolean(boolean: Boolean) {
        dataStore.edit {
            it[BOOLEAN] = boolean
        }
    }

    suspend fun clearToken() {
        dataStore.edit {
            it.clear()
        }
    }

    val email: Flow<String> = dataStore.data.map {
        it[EMAIL] ?: ""
    }

    val name: Flow<String> = dataStore.data.map {
        it[NAME] ?: ""
    }

    val accessToken: Flow<String> = dataStore.data.map {
        it[ACCESS_TOKEN] ?: ""
    }

    val booelan: Flow<Boolean> = dataStore.data.map {
        it[BOOLEAN] ?: false
    }
}