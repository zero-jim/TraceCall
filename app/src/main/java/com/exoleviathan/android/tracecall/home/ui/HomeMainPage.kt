package com.exoleviathan.android.tracecall.home.ui

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.exoleviathan.android.tracecall.common.ui.navigation.NavigationItems
import com.exoleviathan.android.tracecall.home.model.HomeNavigationItems
import com.exoleviathan.android.tracecall.home.viewmodel.CallLogViewModel
import com.exoleviathan.android.tracecall.home.viewmodel.ContactViewModel
import com.exoleviathan.android.tracecall.home.viewmodel.SettingsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeMainPage(callLogViewModel: CallLogViewModel, contactViewModel: ContactViewModel, settingsViewModel: SettingsViewModel) {
    val permissionState = rememberMultiplePermissionsState(
        arrayListOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_CALL_LOG
        )
    )

    when {
        permissionState.allPermissionsGranted -> {
            val navController = rememberNavController()
            val bottomNavigationItems = listOf(HomeNavigationItems.CallLog, HomeNavigationItems.Contact, HomeNavigationItems.Settings)
            Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = { BottomNavigationBar(navController, bottomNavigationItems) }) { innerPadding ->
                val navGraph = remember(navController) {
                    navController.createGraph(startDestination = HomeNavigationItems.CallLog.route) {
                        composable(route = HomeNavigationItems.CallLog.route) {
                            CallLogPage(callLogViewModel, Modifier)
                        }

                        composable(route = HomeNavigationItems.Contact.route) {
                            ContactPage(contactViewModel, Modifier)
                        }

                        composable(route = HomeNavigationItems.Settings.route) {
                            SettingsPage(settingsViewModel, Modifier)
                        }
                    }
                }
                NavHost(navController = navController, navGraph, modifier = Modifier.padding(innerPadding))
            }
        }

        else -> {
            LaunchedEffect(Unit) {
                permissionState.launchMultiplePermissionRequest()
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, navigationItems: List<NavigationItems>) {
    val selectedNavIndex = rememberSaveable { mutableIntStateOf(0) }
    NavigationBar {
        navigationItems.forEachIndexed { index, topLevelRoute ->
            NavigationBarItem(
                icon = { Icon(imageVector = topLevelRoute.icon, contentDescription = topLevelRoute.label) },
                label = { Text(text = topLevelRoute.label, fontSize = 17.sp) },
                selected = selectedNavIndex.intValue == index,
                onClick = {
                    selectedNavIndex.intValue = index
                    navController.navigate(topLevelRoute.route) {
                        navController.popBackStack()
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}