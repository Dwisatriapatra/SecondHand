package com.example.secondhand.datastore

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    suspend fun setBoolean(boolean: Boolean) {
        dataReg.edit {
            it[BOOLEAN] = boolean
        }
    }


    val emailReg: Flow<String> = dataReg.data.map {
        it[EMAIL] ?: ""
    }

    val nameReg: Flow<String> = dataReg.data.map {
        it[NAME] ?: ""
    }

    val passwordReg: Flow<String> = dataReg.data.map {
        it[PASSWORD] ?: ""
    }


    suspend fun deleteData() {
        dataReg.edit {
            it.clear()
        }
    }


}