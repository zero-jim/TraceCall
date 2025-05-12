package com.exoleviathan.android.tracecall.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exoleviathan.android.tracecall.TraceCallApplication
import com.exoleviathan.android.tracecall.common.utils.Logger
import com.exoleviathan.android.tracecall.home.model.CallLogInfo
import com.exoleviathan.android.tracecall.home.model.CallLogIntents
import com.exoleviathan.android.tracecall.home.model.CallLogStates
import com.exoleviathan.android.tracecall.home.repository.CallLogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CallLogViewModel : ViewModel() {
    val callLogIntents = Channel<CallLogIntents>(Channel.UNLIMITED)
    val callLogStates = MutableStateFlow<CallLogStates>(CallLogStates.InitialState)
    private val callLogList = arrayListOf<CallLogInfo>()

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
        Logger.d(TAG, "fetchCallLogInfo")

        val context = TraceCallApplication.getApplicationContext()
        callLogList.clear()

        viewModelScope.launch(Dispatchers.IO) {
            context?.let {
                callLogList.addAll(CallLogRepository.readCallLogs(it))
                Logger.i(TAG, "fetchCallLogInfo", "callLogList size: ${callLogList.size}")

                callLogStates.emit(CallLogStates.CallLogsListUpdated(callLogList))
            }
        }
    }

    companion object {
        private const val TAG = "CallLogViewModel"
    }
}