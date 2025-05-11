package com.exoleviathan.android.tracecall.home.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.exoleviathan.android.tracecall.common.ui.navigation.NavigationItems

sealed class HomeNavigationItems(
    override val label: String,
    override val icon: ImageVector,
    override val route: String
) : NavigationItems(label, icon, route) {
    data object CallLog : HomeNavigationItems("Recent", Icons.Default.Call, "home_call_log")
    data object Contact : HomeNavigationItems("Contacts", Icons.Default.AccountBox, "home_contacts")
    data object Settings : HomeNavigationItems("Settings", Icons.Default.Settings, "home_settings")
}