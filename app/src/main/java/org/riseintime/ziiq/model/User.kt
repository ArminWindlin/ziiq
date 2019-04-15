package org.riseintime.ziiq.model

data class User(
    val name: String,
    val points: Int,
    val bio: String,
    val reports: Int,
    val lang: String
) {
    constructor() : this("", 0, "", 0, "")
}