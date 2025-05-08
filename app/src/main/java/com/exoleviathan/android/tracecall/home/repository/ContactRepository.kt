package com.exoleviathan.android.tracecall.home.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.exoleviathan.android.tracecall.common.utils.Logger
import com.exoleviathan.android.tracecall.home.model.ContactInfo

object ContactRepository {
    private const val TAG = "ContactRepository"

    private const val SIM_CONTENT_AUTHORITY = "icc/adn"
    private const val SIM_COLUMN_NAME = "name"
    private const val SIM_COLUMN_NUMBER = "number"

    fun readSimContacts(context: Context): List<ContactInfo> {
        Logger.d(TAG, "readSimContacts")

        val contactItems = arrayListOf<ContactInfo>()
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(Uri.parse("content://$SIM_CONTENT_AUTHORITY"), null, null, null)

            cursor?.use {
                val nameIndex = it.getColumnIndex(SIM_COLUMN_NAME)
                val numberIndex = it.getColumnIndex(SIM_COLUMN_NUMBER)

                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)?.trim() ?: ""
                    val number = it.getString(numberIndex)?.replace("\\s".toRegex(), "")?.trim() ?: ""

                    Logger.d(TAG, "readSimContacts", "name: $name number: $number")
                    contactItems.add(ContactInfo(name, number))
                }
            } ?: run {
                Logger.lw(TAG, "readSimContacts", "cursor is null or empty")
            }
        } catch (ex: Exception) {
            Logger.le(TAG, "readSimContacts", "error: ${ex.message}")
            ex.printStackTrace()
        } finally {
            cursor?.close()
        }

        return contactItems
    }

    fun readPhoneContacts(context: Context): List<ContactInfo> {
        Logger.d(TAG, "readPhoneContacts")

        val contactItems = arrayListOf<ContactInfo>()
        var cursor: Cursor? = null
        try {
            val selectionArgs = arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI
            )
            cursor = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, selectionArgs, null, null)

            cursor?.use {
                val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val photoUriIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)?.trim() ?: ""
                    val number = it.getString(numberIndex)?.replace("\\s".toRegex(), "")?.trim() ?: ""
                    val photoUri = it.getString(photoUriIndex)

                    Logger.d(TAG, "readPhoneContacts", "name: $name number: $number, photoUri: $photoUri")
                    contactItems.add(ContactInfo(name, number, photoUri))
                }
            } ?: run {
                Logger.lw(TAG, "readPhoneContacts", "cursor is null or empty")
            }
        } catch (ex: Exception) {
            Logger.le(TAG, "readPhoneContacts", "error: ${ex.message}")
            ex.printStackTrace()
        } finally {
            cursor?.close()
        }

        return contactItems
    }
}