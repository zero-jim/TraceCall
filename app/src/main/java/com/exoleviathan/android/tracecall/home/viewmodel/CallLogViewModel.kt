package com.exoleviathan.android.tracecall.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exoleviathan.android.tracecall.home.model.CallLogIntents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CallLogViewModel : ViewModel() {
    val callLogIntents = Channel<CallLogIntents>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            callLogIntents.consumeAsFlow().collect { intent ->
                when (intent) {
                    CallLogIntents.FetchAllCallLogInfo -> {
                        fetchCallLogInfo()
                    }
                }
            }
        }
    }

    private fun fetchCallLogInfo() {

    }
}