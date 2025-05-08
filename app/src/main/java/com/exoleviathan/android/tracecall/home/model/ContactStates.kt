package com.exoleviathan.android.tracecall.home.model

sealed class ContactStates {
    data object InitialState : ContactStates()
    data class ContactListUpdated(val contactList: List<ContactInfo>) : ContactStates()
}