package com.exoleviathan.android.tracecall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.exoleviathan.android.tracecall.common.utils.Logger
import com.exoleviathan.android.tracecall.common.utils.NavigationHelper

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(TAG, "onCreate")

        enableEdgeToEdge()
        NavigationHelper.navigateToHomeActivity(this) {
            finish()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}