package org.riseintime.ziiq.model

data class Question(
    val text: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val user: String,
    val lang: String,
    val correctAnswer: Int,
    val randomInt: Int,
    val solves: Int,
    val likes: Int,
    val dislikes: Int
) {
    constructor() : this(
        "", "", "", "", "", "", "",
        0, 0, 0, 0, 0
    )
}