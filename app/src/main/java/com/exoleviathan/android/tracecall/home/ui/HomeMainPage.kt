package com.exoleviathan.android.tracecall.home.ui

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.exoleviathan.android.tracecall.home.viewmodel.CallLogViewModel
import com.exoleviathan.android.tracecall.home.viewmodel.ContactViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeMainPage(callLogViewModel: CallLogViewModel, contactViewModel: ContactViewModel, modifier: Modifier) {
    val permissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)

    when {
        permissionState.status.isGranted -> ContactPage(contactViewModel, modifier)

        else -> {
            LaunchedEffect(Unit) {
                permissionState.launchPermissionRequest()
            }
        }
    }
}