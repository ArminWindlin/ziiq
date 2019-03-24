package org.riseintime.ziiq.model

data class Question(
    val id: String,
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
    val choice1: Int,
    val choice2: Int,
    val choice3: Int,
    val choice4: Int,
    val likes: Int,
    val dislikes: Int
) {
    constructor() : this(
        "", "", "", "", "", "", "", "",
        0, 0, 0, 0, 0, 0, 0, 0, 0
    )
}