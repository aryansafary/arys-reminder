package com.arysapp.task.ui.components

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.arysapp.task.ui.viewmodel.DataStoreViewmodel
import com.arysapp.task.utils.Constants.USER_LANGUAGE

@Composable
fun AppConfig(
    datastore: DataStoreViewmodel = hiltViewModel()
) {
    getDataStoreVariables(datastore)
}

private fun getDataStoreVariables(datastore: DataStoreViewmodel) {
    USER_LANGUAGE = datastore.getLanguage()
}