package com.exoleviathan.android.tracecall.home.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exoleviathan.android.tracecall.TraceCallApplication
import com.exoleviathan.android.tracecall.common.utils.Logger
import com.exoleviathan.android.tracecall.home.model.ContactInfo
import com.exoleviathan.android.tracecall.home.model.ContactIntents
import com.exoleviathan.android.tracecall.home.model.ContactStates
import com.exoleviathan.android.tracecall.home.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ContactViewModel : ViewModel() {
    val contactIntents = Channel<ContactIntents>(Channel.UNLIMITED)
    val contactListState = MutableStateFlow<ContactStates>(ContactStates.InitialState)
    private val contactSet = mutableSetOf<ContactInfo>()

    init {
        Logger.d(TAG, "init")

        viewModelScope.launch {
            contactIntents.consumeAsFlow().collect { intent ->
                Logger.i(TAG, "init", "intent: $intent")

                when (intent) {
                    ContactIntents.FetchAllUniqueContactList -> {
                        fetchUniqueContactList()
                    }

                    is ContactIntents.CopyContactInfoToClipboard -> {
                        copyContactInfoToClipboard(intent.contactInfo)
                    }
                }
            }
        }
    }

    private fun fetchUniqueContactList() {
        Logger.d(TAG, "fetchUniqueContactList")

        val context = TraceCallApplication.getApplicationContext()
        contactSet.clear()

        context?.let {
            viewModelScope.launch(Dispatchers.IO) {
                contactSet.addAll(ContactRepository.readSimContacts(it))
                contactSet.addAll(ContactRepository.readPhoneContacts(it))

                contactSet.sorted()
                Logger.li(TAG, "fetchUniqueContactList", "listSize: ${contactSet.size}")

                contactListState.emit(ContactStates.ContactListUpdated(contactSet.toList()))
            }
        }
    }

    private fun copyContactInfoToClipboard(contactInfo: ContactInfo?) {
        Logger.d(TAG, "copyContactInfoToClipboard", "contactInfo: $contactInfo")

        try {
            val clipboardManager = TraceCallApplication.getApplicationContext()?.getSystemService(ClipboardManager::class.java)
            val clipData = ClipData.newPlainText(contactInfo?.contactName, contactInfo?.contactNumber)
            clipboardManager?.setPrimaryClip(clipData)
        } catch (ex: Exception) {
            Logger.le(TAG, "copyContactInfoToClipboard", "error: ${ex.message}")
            ex.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "ContactViewModel"
    }
}