package com.exoleviathan.android.tracecall.common.utils

import android.util.Log

object Logger {

    fun d(className: String, methodName: String, message: String? = null) {
        Log.d(className, "$methodName() ${message ?: "enter"}")
    }

    fun i(className: String, methodName: String, message: String? = null) {
        Log.i(className, "$methodName() ${message ?: "enter"}")
    }

    fun v(className: String, methodName: String, message: String? = null) {
        Log.v(className, "$methodName() ${message ?: "enter"}")
    }

    fun w(className: String, methodName: String, message: String? = null) {
        Log.w(className, "$methodName() ${message ?: "enter"}")
    }

    fun e(className: String, methodName: String, message: String? = null) {
        Log.e(className, "$methodName() ${message ?: "enter"}")
    }

    fun li(className: String, methodName: String, message: String? = null) {
        Log.i(className, "$methodName() ${message ?: "enter"}")
    }

    fun lw(className: String, methodName: String, message: String? = null) {
        Log.w(className, "$methodName() ${message ?: "enter"}")
    }

    fun le(className: String, methodName: String, message: String? = null) {
        Log.e(className, "$methodName() ${message ?: "enter"}")
    }
}