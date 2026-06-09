package com.arysapp.task

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.arysapp.task.navigation.Screens
import com.arysapp.task.navigation.SetupNavGraph
import com.arysapp.task.navigation.topbar.MyTopAppBar
import com.arysapp.task.ui.components.AppConfig
import com.arysapp.task.ui.components.ChangeStatusBarColor
import com.arysapp.task.ui.components.MyFloatingActionButton
import com.arysapp.task.ui.components.PermissionRequest
import com.arysapp.task.ui.theme.ArysTaskTheme
import com.arysapp.task.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.task.utils.Constants.USER_LANGUAGE
import com.arysapp.task.utils.helper.LocaleUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureLockScreenVisibility()
        enableEdgeToEdge()

        setContent {
            ArysTaskTheme {
                navController = rememberNavController()
                ChangeStatusBarColor(navController)
                AppConfig()
                LocaleUtils.setLocale(LocalContext.current, USER_LANGUAGE)
                CompositionLocalProvider(
                    LocalLayoutDirection provides
                            if (USER_LANGUAGE == PERSIAN_LANGUAGE) LayoutDirection.Rtl
                            else LayoutDirection.Ltr
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            MyTopAppBar(
                                navController = navController,
                                onSettingsClick = {
                                    navController.navigate(Screens.Settings.route)
                                }
                            )
                        },
                        floatingActionButton = {
                            MyFloatingActionButton(navController)
                        },
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            PermissionRequest { }
                            SetupNavGraph(
                                navController = navController,
                            )
                        }
                    }
                }

                LaunchedEffect(intent) {
                    handleAlarmIntent(intent)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAlarmIntent(intent)
    }

    private fun handleAlarmIntent(intent: Intent?) {
        if (intent?.getStringExtra("OPEN_SCREEN") == "SHOW_ALARM") {
            val taskId = intent.getLongExtra("TASK_ID", -1L)
            val title = intent.getStringExtra("TASK_TITLE") ?: ""
            val desc = intent.getStringExtra("TASK_DESC") ?: ""

            navController.navigate("${Screens.ShowAlarmScreen.route}?taskId=$taskId&title=$title&desc=$desc") {
                popUpTo(Screens.Home.route) { inclusive = false }
            }
        }
    }

    private fun configureLockScreenVisibility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
    }
}