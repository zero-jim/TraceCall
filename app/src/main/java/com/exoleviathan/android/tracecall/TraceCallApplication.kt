package com.exoleviathan.android.tracecall

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.exoleviathan.android.tracecall.common.utils.Logger

class TraceCallApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.d(TAG, "onCreate")

        context = applicationContext
    }

    override fun onTerminate() {
        super.onTerminate()
        Logger.d(TAG, "onTerminate")

        context = null
    }

    companion object {
        private const val TAG = "TraceCallApplication"

        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        @Synchronized
        fun getApplicationContext(): Context? {
            return synchronized(this) {
                context
            }
        }
    }
}