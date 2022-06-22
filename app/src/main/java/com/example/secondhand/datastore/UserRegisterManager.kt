package com.example.secondhand.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey

class UserRegisterManager(context: Context) {

    private val dataReg: DataStore<Preferences> = context.createDataStore(name = "user_reg")

    companion object {
        val EMAIL = preferencesKey<String>("USER_EMAIL")
        val NAME = preferencesKey<String>("USER_NAME")
        val PASSWORD = preferencesKey<String>("USER_PASSWORD")
        val BOOLEAN = preferencesKey<Boolean>("BOOLEAN")
    }

    suspend fun saveData(email: String, name: String, password: String) {
        dataReg.edit {
            it[EMAIL] = email
            it[NAME] = name
            it[PASSWORD] = password
        }

    }


}