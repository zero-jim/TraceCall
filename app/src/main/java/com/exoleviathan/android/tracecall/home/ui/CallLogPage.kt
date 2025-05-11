package com.exoleviathan.android.tracecall.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.exoleviathan.android.tracecall.home.model.CallLogIntents
import com.exoleviathan.android.tracecall.home.viewmodel.CallLogViewModel

@Composable
fun CallLogPage(callLogViewModel: CallLogViewModel, modifier: Modifier) {
    LaunchedEffect(Unit) {
        callLogViewModel.callLogIntents.send(CallLogIntents.FetchAllCallLogInfo)
    }
}