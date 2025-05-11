package com.exoleviathan.android.tracecall.home.model

sealed class CallLogIntents {
    data object FetchAllCallLogInfo : CallLogIntents()
}