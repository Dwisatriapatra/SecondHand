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
        val PASSWORD = preferencesKey<String>("PASSWORD")
        val BOOLEAN = preferencesKey<Boolean>("BOOLEAN")
        val IS_USER = preferencesKey<Boolean>("IS_USER")

        //save data ewhen update user
        val FOTOUSER = preferencesKey<String>("FOTOUSER")
        val KOTA = preferencesKey<String>("KOTA")
        val ALAMAT = preferencesKey<String>("ALAMAT")
        val NO_HANDPHONE = preferencesKey<String>("NOHANDPHONE")

    }

    suspend fun saveToken(
        email: String,
        name: String,
        accessToken: String,
        password: String
    ) {
        dataStore.edit {
            it[EMAIL] = email
            it[NAME] = name
            it[ACCESS_TOKEN] = accessToken
            it[PASSWORD] = password
        }
    }

    suspend fun saveUpdateAkun(
        fotoUser: String,
        kota: String,
        alamat: String,
        noHandphone: String
    ) {
        dataStore.edit {
            it[FOTOUSER] = fotoUser
            it[KOTA] = kota
            it[ALAMAT] = alamat
            it[NO_HANDPHONE] = noHandphone
        }
    }

    suspend fun setBoolean(boolean: Boolean) {
        dataStore.edit {
            it[BOOLEAN] = boolean
        }
    }

    suspend fun setIsUser(boolean: Boolean) {
        dataStore.edit {
            it[IS_USER] = boolean
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

    val password: Flow<String> = dataStore.data.map {
        it[PASSWORD] ?: ""
    }

    val isUser: Flow<Boolean> = dataStore.data.map {
        it[IS_USER] ?: false
    }

}