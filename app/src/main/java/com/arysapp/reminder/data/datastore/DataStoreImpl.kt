package com.arysapp.reminder.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arysapp.reminder.utils.Constants.DATASTORE_NAME
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)
class DataStoreImpl @Inject constructor(
    private val context: Context)
    : DataStoreRepository {
    override suspend fun putString(key: String, value: String) {
      val preferencesKey = stringPreferencesKey(key)
        context.datastore.edit { preferences->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getString(key: String): String? {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            val preferences = context.datastore.data.first()
            preferences[preferencesKey]
        }catch (e:Exception){
            e.printStackTrace()
            null
        }

    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        context.datastore.edit { preferences->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getBoolean(key: String): Boolean? {
        return try {
            val preferencesKey = booleanPreferencesKey(key)
            val preferences = context.datastore.data.first()
            preferences[preferencesKey]
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

    override suspend fun putInt(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        context.datastore.edit { preferences->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getInt(key: String): Int? {
        return try {
            val preferencesKey = intPreferencesKey(key)
            val preferences = context.datastore.data.first()
            preferences[preferencesKey]
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

}