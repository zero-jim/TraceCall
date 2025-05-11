package com.exoleviathan.android.tracecall.home.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.exoleviathan.android.tracecall.R
import com.exoleviathan.android.tracecall.TraceCallApplication
import com.exoleviathan.android.tracecall.common.ui.navigation.NavigationItems

sealed class HomeNavigationItems(
    override val label: String,
    override val icon: ImageVector,
    override val route: String
) : NavigationItems(label, icon, route) {
    data object CallLog : HomeNavigationItems(TraceCallApplication.getApplicationContext()?.getString(R.string.recent) ?: "Recent", Icons.Default.Call, "home_call_log")
    data object Contact : HomeNavigationItems(TraceCallApplication.getApplicationContext()?.getString(R.string.contacts) ?: "Contacts", Icons.Default.AccountBox, "home_contacts")
    data object Settings : HomeNavigationItems(TraceCallApplication.getApplicationContext()?.getString(R.string.settings) ?: "Settings", Icons.Default.Settings, "home_settings")
}