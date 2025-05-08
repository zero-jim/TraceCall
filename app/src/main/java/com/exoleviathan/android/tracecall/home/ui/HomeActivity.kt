package com.exoleviathan.android.tracecall.home.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.exoleviathan.android.tracecall.common.ui.theme.TraceCallTheme
import com.exoleviathan.android.tracecall.common.utils.Logger
import com.exoleviathan.android.tracecall.home.viewmodel.CallLogViewModel
import com.exoleviathan.android.tracecall.home.viewmodel.ContactViewModel

class HomeActivity : ComponentActivity() {
    private val callLogViewModel: CallLogViewModel by viewModels()
    private val contactViewModel: ContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(TAG, "onCreate")

        enableEdgeToEdge()
        setContent {
            TraceCallTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeMainPage(callLogViewModel, contactViewModel, Modifier.padding(innerPadding))
                }
            }
        }
    }

    companion object {
        private const val TAG = "HomeActivity"
    }
}