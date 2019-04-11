package org.riseintime.ziiq

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_question_detail.*
import org.riseintime.ziiq.model.Question

class QuestionDetailActivity : AppCompatActivity() {
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val TAG = MainActivity::class.java.simpleName

    private var correctAnswer: Int = 0
    private var activeQuestion = true
    private lateinit var question: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)

        val questionId = intent.getStringExtra("question_id")

        initializeQuestion(questionId)
    }

    private fun initializeQuestion(questionId: String) {
        activeQuestion = true
        db.collection("questions").document(questionId).get()
            .addOnSuccessListener { result ->
                question = result.toObject(Question::class.java)!!
                main_question_text.text = question.text
                var total = question.choice1 + question.choice2 + question.choice3 + question.choice4
                if (total == 0) total = 1
                main_option_1.text = question.answer1 + "\n\n" + (question.choice1 * 100 / total).toInt() + "%"
                main_option_2.text = question.answer2 + "\n\n" + (question.choice2 * 100 / total).toInt() + "%"
                main_option_3.text = question.answer3 + "\n\n" + (question.choice3 * 100 / total).toInt() + "%"
                main_option_4.text = question.answer4 + "\n\n" + (question.choice4 * 100 / total).toInt() + "%"
                correctAnswer = question.correctAnswer

                main_result_text.text =
                    "Solves: " + question.solves + "\nLikes: " + question.likes + "\nDislikes: " + question.dislikes

                // Mark correct answer
                when (correctAnswer) {
                    1 -> {
                        val cover1 = findViewById<View>(R.id.main_option_1) as Button
                        cover1.setBackgroundResource(R.drawable.button_option_green)
                    }
                    2 -> {
                        val cover2 = findViewById<View>(R.id.main_option_2) as Button
                        cover2.setBackgroundResource(R.drawable.button_option_green)
                    }
                    3 -> {
                        val cover3 = findViewById<View>(R.id.main_option_3) as Button
                        cover3.setBackgroundResource(R.drawable.button_option_green)
                    }
                    4 -> {
                        val cover4 = findViewById<View>(R.id.main_option_4) as Button
                        cover4.setBackgroundResource(R.drawable.button_option_green)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE", "Error getting documents: ", exception)
            }
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
            R.id.action_ranking -> {
                startActivity(Intent(this, RankingActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

