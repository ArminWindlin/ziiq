package org.riseintime.ziiq.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class User(
    val id: String,
    val name: String,
    val points: Int,
    val reports: Int,
    val lang: String,
    @ServerTimestamp val timestamp: Timestamp? = null
) {
    constructor() : this("", "", 0, 0, "")
}