package com.exoleviathan.android.tracecall.home.model

data class ContactInfo(
    val contactName: String,
    val contactNumber: String,
    val contactPhotoUri: String? = null
) : Comparable<ContactInfo> {

    override fun compareTo(other: ContactInfo): Int {
        return if (this.contactName == other.contactName) {
            this.contactNumber.compareTo(other.contactNumber)
        } else {
            this.contactName.compareTo(other.contactName)
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactInfo

        if (contactName != other.contactName) return false
        if (contactNumber != other.contactNumber) return false

        return true
    }
}