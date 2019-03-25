package org.riseintime.ziiq

import android.content.Intent
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


class MainActivity : AppCompatActivity() {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val TAG = MainActivity::class.java.simpleName

    private var correctAnswer: Int = 0
    private var selectedAnswer: Int = -1
    private lateinit var questionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initializeQuestion()
    }

    fun initializeQuestion(view: View = findViewById(android.R.id.content)) {
        changeUIBack()
        FirebaseFirestore.getInstance().collection("questions")
            .whereLessThan("randomInt", (0..Int.MAX_VALUE).random())
            .orderBy("randomInt", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { result ->
                val question = result.first().toObject(Question::class.java)
                questionId = question.id
                main_question_text.text = question.text
                main_option_1.text = question.answer1
                main_option_2.text = question.answer2
                main_option_3.text = question.answer3
                main_option_4.text = question.answer4
                correctAnswer = question.correctAnswer
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE", "Error getting documents: ", exception)
            }
    }

    fun newQuestion(view: View) {
        initializeQuestion()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                openSettingsActivity()
                true
            }
            R.id.action_my_questions -> {
                openMyQuestionsActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun submitAnswer(view: View) {
        changeUI()

        when (view.getId()) {
            R.id.main_option_1 ->
                selectedAnswer = 1
            R.id.main_option_2 ->
                selectedAnswer = 2
            R.id.main_option_3 ->
                selectedAnswer = 3
            R.id.main_option_4 ->
                selectedAnswer = 4
        }

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

        updateQuestion(selectedAnswer)
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
    }

    private fun updateQuestion(choice: Int) {
        val qDocRef = FirebaseFirestore.getInstance().collection("questions").document(questionId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(qDocRef)
            val newSolves = snapshot.getLong("solves")!! + 1
            transaction.update(qDocRef, "solves", newSolves)
            when (choice) {
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

    private fun openSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun openMyQuestionsActivity() {
        val intent = Intent(this, MyQuestionsActivity::class.java)
        startActivity(intent)
    }
}
