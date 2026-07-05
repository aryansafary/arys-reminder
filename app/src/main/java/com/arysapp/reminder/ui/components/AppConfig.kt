package com.arysapp.reminder.ui.components

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.arysapp.reminder.ui.viewmodel.DataStoreViewmodel
import com.arysapp.reminder.utils.Constants.USER_LANGUAGE

@Composable
fun AppConfig(
    datastore: DataStoreViewmodel = hiltViewModel()
) {
    getDataStoreVariables(datastore)
}

private fun getDataStoreVariables(datastore: DataStoreViewmodel) {
    USER_LANGUAGE = datastore.getLanguage()
}