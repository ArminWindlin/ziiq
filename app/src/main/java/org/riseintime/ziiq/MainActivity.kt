package org.riseintime.ziiq

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.riseintime.ziiq.fragment.MyAccountFragment
import org.riseintime.ziiq.model.Question
import org.riseintime.ziiq.recyclerview.item.QuestionItem
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.act
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val TAG = MainActivity::class.java.simpleName

    private var correctAnswer: Int = 0
    private var selectedAnswer: Int = -1
    private var activeQuestion = true
    private var loading = true
    private lateinit var questionId: String
    private lateinit var question: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initializeQuestion()
    }

    private fun initializeQuestion() {
        activeQuestion = true
        changeUIBack()
        FirebaseFirestore.getInstance().collection("questions")
            .whereEqualTo(
                "lang",
                Locale.getDefault().getLanguage()
            )
            .whereLessThan("randomInt", (0..Int.MAX_VALUE).random())
            .orderBy("randomInt", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { result ->
                question = result.first().toObject(Question::class.java)
                questionId = question.id
                main_question_text.text = question.text
                main_option_1.text = question.answer1
                main_option_2.text = question.answer2
                main_option_3.text = question.answer3
                main_option_4.text = question.answer4
                correctAnswer = question.correctAnswer
                loading = false
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun newQuestion(view: View) {
        initializeQuestion()
        var like = false
        when (view.getId()) {
            R.id.main_button_dislike ->
                like = false
            R.id.main_button_like ->
                like = true
        }

        updateQuestion(like)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_my_questions -> {
                startActivity(Intent(this, MyQuestionsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun submitAnswer(view: View) {
        if (loading) return
        if (!activeQuestion) return
        activeQuestion = false

        when (view.getId()) {
            R.id.main_option_cover_1 ->
                selectedAnswer = 1
            R.id.main_option_cover_2 ->
                selectedAnswer = 2
            R.id.main_option_cover_3 ->
                selectedAnswer = 3
            R.id.main_option_cover_4 ->
                selectedAnswer = 4
        }

        changeUI()

        /*
        val builder = AlertDialog.Builder(this@MainActivity)
        if (selectedAnswer == correctAnswer)
            builder.setTitle("Correct!")
        else
            builder.setTitle("Wrong!")
        builder.setMessage("Nice!")
        builder.setPositiveButton("Next Question") { _, _ ->
            initializeQuestion()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        */
    }

    private fun changeUI() {
        val ratingContainer = findViewById<View>(R.id.main_rating_container) as ConstraintLayout
        val questionText = findViewById<View>(R.id.main_question_text) as TextView
        val resultText = findViewById<View>(R.id.main_result_text) as TextView

        val ratingContainerParams: ConstraintLayout.LayoutParams =
            ratingContainer.layoutParams as ConstraintLayout.LayoutParams
        val questionTextParams: ConstraintLayout.LayoutParams =
            questionText.layoutParams as ConstraintLayout.LayoutParams
        val resultTextParams: ConstraintLayout.LayoutParams =
            resultText.layoutParams as ConstraintLayout.LayoutParams

        ratingContainerParams.matchConstraintPercentHeight = 0.1F
        ratingContainer.layoutParams = ratingContainerParams

        questionTextParams.matchConstraintPercentHeight = 0.2F
        questionText.layoutParams = questionTextParams

        resultTextParams.matchConstraintPercentHeight = 0.1F
        resultText.layoutParams = resultTextParams
        if (selectedAnswer == correctAnswer)
            resultText.text = getString(R.string.correct_answer)
        else
            resultText.text = getString(R.string.wrong_answer)

        // Mark selected answer
        when (selectedAnswer) {
            1 -> {
                val cover1 = findViewById<View>(R.id.main_option_cover_1) as Button
                cover1.setBackgroundResource(R.drawable.button_border_red)
            }
            2 -> {
                val cover2 = findViewById<View>(R.id.main_option_cover_2) as Button
                cover2.setBackgroundResource(R.drawable.button_border_red)
            }
            3 -> {
                val cover3 = findViewById<View>(R.id.main_option_cover_3) as Button
                cover3.setBackgroundResource(R.drawable.button_border_red)
            }
            4 -> {
                val cover4 = findViewById<View>(R.id.main_option_cover_4) as Button
                cover4.setBackgroundResource(R.drawable.button_border_red)
            }
        }

        // Mark correct answer
        when (correctAnswer) {
            1 -> {
                val cover1 = findViewById<View>(R.id.main_option_cover_1) as Button
                cover1.setBackgroundResource(R.drawable.button_border_green)
            }
            2 -> {
                val cover2 = findViewById<View>(R.id.main_option_cover_2) as Button
                cover2.setBackgroundResource(R.drawable.button_border_green)
            }
            3 -> {
                val cover3 = findViewById<View>(R.id.main_option_cover_3) as Button
                cover3.setBackgroundResource(R.drawable.button_border_green)
            }
            4 -> {
                val cover4 = findViewById<View>(R.id.main_option_cover_4) as Button
                cover4.setBackgroundResource(R.drawable.button_border_green)
            }
        }

        // Add question percentages
        var total = question.choice1 + question.choice2 + question.choice3 + question.choice4 + 1
        if (total == 0) total = 1
        main_option_cover_1.text = "\n\n\n" + (question.choice1 * 100 / total).toInt() + "%"
        main_option_cover_2.text = "\n\n\n" + (question.choice2 * 100 / total).toInt() + "%"
        main_option_cover_3.text = "\n\n\n" + (question.choice3 * 100 / total).toInt() + "%"
        main_option_cover_4.text = "\n\n\n" + (question.choice4 * 100 / total).toInt() + "%"

        // Change button colors
        val c11 = Random.nextInt(0, 255)
        val c12 = Random.nextInt(0, 255)
        val c13 = Random.nextInt(0, 255)
        var c21 = c11 + 100
        if (c21 > 255) c21 = 255
        var c22 = c12
        var c23 = c13
        var c31 = c11
        var c32 = c12 + 100
        if (c32 > 255) c32 = 255
        var c33 = c13
        var c41 = c11
        var c42 = c12
        var c43 = c13 + 100
        if (c43 > 255) c43 = 255
        main_option_1.setBackgroundColor(Color.rgb(c11, c12, c13))
        main_option_2.setBackgroundColor(Color.rgb(c21, c22, c23))
        main_option_3.setBackgroundColor(Color.rgb(c31, c32, c33))
        main_option_4.setBackgroundColor(Color.rgb(c41, c42, c43))
        var fontColor = Color.BLACK
        if (c11 * 0.299 + c12 * 0.587 + c13 * 0.114 < 186)
            fontColor = Color.WHITE
        main_option_1.setTextColor(fontColor)
        main_option_2.setTextColor(fontColor)
        main_option_3.setTextColor(fontColor)
        main_option_4.setTextColor(fontColor)

    }

    private fun changeUIBack() {
        val ratingContainer = findViewById<View>(R.id.main_rating_container) as ConstraintLayout
        val questionText = findViewById<View>(R.id.main_question_text) as TextView
        val resultText = findViewById<View>(R.id.main_result_text) as TextView

        val ratingContainerParams: ConstraintLayout.LayoutParams =
            ratingContainer.layoutParams as ConstraintLayout.LayoutParams
        val questionTextParams: ConstraintLayout.LayoutParams =
            questionText.layoutParams as ConstraintLayout.LayoutParams
        val resultTextParams: ConstraintLayout.LayoutParams =
            resultText.layoutParams as ConstraintLayout.LayoutParams

        ratingContainerParams.matchConstraintPercentHeight = 0F
        ratingContainer.layoutParams = ratingContainerParams

        questionTextParams.matchConstraintPercentHeight = 0.4F
        questionText.layoutParams = questionTextParams

        resultTextParams.matchConstraintPercentHeight = 0F
        resultText.layoutParams = resultTextParams
        resultText.text = ""

        // remove answer marks
        val cover1 = findViewById<View>(R.id.main_option_cover_1) as Button
        cover1.setBackgroundResource(R.color.transparent)
        val cover2 = findViewById<View>(R.id.main_option_cover_2) as Button
        cover2.setBackgroundResource(R.color.transparent)
        val cover3 = findViewById<View>(R.id.main_option_cover_3) as Button
        cover3.setBackgroundResource(R.color.transparent)
        val cover4 = findViewById<View>(R.id.main_option_cover_4) as Button
        cover4.setBackgroundResource(R.color.transparent)
        // remove percentages
        main_option_cover_1.text = ""
        main_option_cover_2.text = ""
        main_option_cover_3.text = ""
        main_option_cover_4.text = ""
    }

    private fun updateQuestion(like: Boolean) {
        val qDocRef = FirebaseFirestore.getInstance().collection("questions").document(questionId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(qDocRef)
            val newSolves = snapshot.getLong("solves")!! + 1
            transaction.update(qDocRef, "solves", newSolves)
            if (like)
                transaction.update(qDocRef, "likes", snapshot.getLong("likes")!! + 1)
            else
                transaction.update(qDocRef, "dislikes", snapshot.getLong("dislikes")!! + 1)
            when (selectedAnswer) {
                1 ->
                    transaction.update(qDocRef, "choice1", snapshot.getLong("choice1")!! + 1)
                2 ->
                    transaction.update(qDocRef, "choice2", snapshot.getLong("choice2")!! + 1)
                3 ->
                    transaction.update(qDocRef, "choice3", snapshot.getLong("choice3")!! + 1)
                4 ->
                    transaction.update(qDocRef, "choice4", snapshot.getLong("choice4")!! + 1)
            }

            // Success
            null
        }.addOnSuccessListener { Log.d(TAG, "Increased solves of question! $questionId") }
            .addOnFailureListener { e -> Log.w(TAG, "Failed to update question $questionId", e) }
    }

}
