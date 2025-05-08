package com.exoleviathan.android.tracecall.common.utils

import android.content.Context
import android.content.Intent
import com.exoleviathan.android.tracecall.home.ui.HomeActivity

object NavigationHelper {

    fun navigateToHomeActivity(context: Context, onComplete: () -> Unit = {}) {
        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        context.startActivity(intent)

        onComplete.invoke()
    }
}