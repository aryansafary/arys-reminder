package com.arysapp.task.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arysapp.task.data.datastore.DataStoreRepository
import com.arysapp.task.utils.Constants.PERSIAN_LANGUAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DataStoreViewmodel @Inject constructor(
    private val repository: DataStoreRepository
): ViewModel()
{
    companion object{
        const val USER_LANGUAGE_KEY = "USER_LANGUAGE_KEY"
    }
fun setLanguage(language: String) {
    viewModelScope.launch {
        repository.putString(USER_LANGUAGE_KEY, language)
    }
}

    fun getLanguage() = runBlocking {   repository.getString(USER_LANGUAGE_KEY)?:PERSIAN_LANGUAGE }

}