package com.exoleviathan.android.tracecall.home.model

import java.util.TreeSet

data class CallLogInfo(
    val contactInfo: ContactInfo,
    val details: TreeSet<CallLogDetailsInfo>
) {
    override fun equals(other: Any?): Boolean {
        return this.contactInfo.contactNumber == (other as CallLogInfo).contactInfo.contactNumber
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

data class CallLogDetailsInfo(
    val date: Long,
    val type: Int,
    val duration: Long
) : Comparable<CallLogDetailsInfo> {

    override fun compareTo(other: CallLogDetailsInfo): Int {
        return this.date.compareTo(other.date)
    }
}