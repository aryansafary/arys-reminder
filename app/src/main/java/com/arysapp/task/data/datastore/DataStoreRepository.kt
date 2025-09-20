package com.arysapp.task.data.datastore

interface DataStoreRepository {

    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String): Boolean?
    suspend fun putInt(key: String, value: Int)
    suspend fun getInt(key: String): Int?
}