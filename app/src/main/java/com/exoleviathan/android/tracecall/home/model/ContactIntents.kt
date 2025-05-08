package com.exoleviathan.android.tracecall.home.model

sealed class ContactIntents {
    data object FetchAllUniqueContactList : ContactIntents()
    data class CopyContactInfoToClipboard(val contactInfo: ContactInfo?) : ContactIntents()
}