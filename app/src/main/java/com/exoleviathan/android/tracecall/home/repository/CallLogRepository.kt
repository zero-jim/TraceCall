package com.exoleviathan.android.tracecall.home.repository

import android.content.Context
import android.database.Cursor
import android.provider.CallLog.Calls
import com.exoleviathan.android.tracecall.common.utils.Logger
import com.exoleviathan.android.tracecall.home.model.CallLogDetailsInfo
import com.exoleviathan.android.tracecall.home.model.CallLogInfo
import com.exoleviathan.android.tracecall.home.model.ContactInfo
import java.util.TreeSet

object CallLogRepository {
    private const val TAG = "CallLogRepository"
    private const val LIMIT_PARAM_VALUE = "100"

    fun readCallLogs(context: Context): List<CallLogInfo> {
        Logger.d(TAG, "readCallLogs")

        val callLogs = arrayListOf<CallLogInfo>()
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(
                Calls.CONTENT_URI.buildUpon().appendQueryParameter(Calls.LIMIT_PARAM_KEY, LIMIT_PARAM_VALUE).build(),
                null,
                null,
                null,
                Calls.DEFAULT_SORT_ORDER
            )
            cursor?.use {
                val nameIndex = it.getColumnIndex(Calls.CACHED_NAME)
                val numberIndex = it.getColumnIndex(Calls.NUMBER)
                val photoIndex = it.getColumnIndex(Calls.CACHED_PHOTO_URI)
                val dateIndex = it.getColumnIndex(Calls.DATE)
                val callType = it.getColumnIndex(Calls.TYPE)
                val durationIndex = it.getColumnIndex(Calls.DURATION)

                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)?.trim() ?: ""
                    val number = it.getString(numberIndex)?.replace("\\s".toRegex(), "")?.trim() ?: ""
                    val photoUrl = it.getString(photoIndex)
                    val date = it.getLong(dateIndex)
                    val type = it.getInt(callType)
                    val duration = it.getLong(durationIndex)

                    val callLogDetailsInfo = CallLogDetailsInfo(date, type, duration)

                    callLogs.find { info ->
                        info.contactInfo.contactNumber == number
                    }?.details?.add(callLogDetailsInfo) ?: run {
                        Logger.i(TAG, "readCallLogs", "number is not present in created list")

                        val contactInfo = ContactInfo(name, number, photoUrl)
                        val callLogDetails = TreeSet<CallLogDetailsInfo>()

                        callLogDetails.add(callLogDetailsInfo)
                        callLogs.add(CallLogInfo(contactInfo,callLogDetails))
                    }

                    Logger.d(TAG, "readCallLogs", "name: $name number: $number date: $date")
                }

                Logger.d(TAG, "readCallLogs", "callLogs: $callLogs")
            } ?: run {
                Logger.lw(TAG, "readCallLogs", "cursor is null or empty")
            }
        } catch (ex: Exception) {
            Logger.le(TAG, "readCallLogs", "error: ${ex.message}")
            ex.printStackTrace()
        } finally {
            cursor?.close()
        }

        Logger.li(TAG, "readCallLogs", "callLogs size: ${callLogs.size}")
        return callLogs
    }
}