package com.exoleviathan.android.tracecall.home.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.exoleviathan.android.tracecall.common.ui.theme.TraceCallTheme
import com.exoleviathan.android.tracecall.common.utils.Logger
import com.exoleviathan.android.tracecall.home.viewmodel.CallLogViewModel
import com.exoleviathan.android.tracecall.home.viewmodel.ContactViewModel
import com.exoleviathan.android.tracecall.home.viewmodel.SettingsViewModel

class HomeActivity : ComponentActivity() {
    private val callLogViewModel: CallLogViewModel by viewModels()
    private val contactViewModel: ContactViewModel by viewModels()
    private val settingsViewModel : SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(TAG, "onCreate")

        enableEdgeToEdge()
        setContent {
            TraceCallTheme {
                HomeMainPage(callLogViewModel, contactViewModel, settingsViewModel)
            }
        }
    }

    companion object {
        private const val TAG = "HomeActivity"
    }
}