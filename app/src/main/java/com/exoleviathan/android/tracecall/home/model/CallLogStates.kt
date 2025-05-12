package com.exoleviathan.android.tracecall.home.model

sealed class CallLogStates {
    data object InitialState : CallLogStates()
    data class CallLogsListUpdated(val callLogList: List<CallLogInfo>) : CallLogStates()
}