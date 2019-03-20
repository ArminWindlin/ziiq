package org.riseintime.ziiq

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xwray.groupie.kotlinandroidextensions.Item

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.riseintime.ziiq.fragment.MyAccountFragment
import org.riseintime.ziiq.model.Question
import org.riseintime.ziiq.recyclerview.item.QuestionItem

class MainActivity : AppCompatActivity() {

    private var correctAnswer: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initializeQuestion()
    }

    fun initializeQuestion() {
        FirebaseFirestore.getInstance().collection("questions")
            .whereLessThan("randomInt", (0..Int.MAX_VALUE).random())
            .orderBy("randomInt", Query.Direction.DESCENDING).limit(1).get()
            .addOnSuccessListener { result ->
                val question = result.first().toObject(Question::class.java)
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
        var selectedAnswer: Int = -1
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



        val builder = AlertDialog.Builder(this@MainActivity)

        if (selectedAnswer == correctAnswer)
            builder.setTitle("Correct!")
        else
            builder.setTitle("Wrong!")

        builder.setMessage("Nice!")

        builder.setPositiveButton("Next Question") { _, _ ->
            initializeQuestion()
        }

        /*      builder.setNeutralButton(R.string.participation_home) { _, _ ->
                  val intent = Intent(this, StoriesActivity::class.java)
                  startActivity(intent)
              }*/

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    fun openSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun openMyQuestionsActivity() {
        val intent = Intent(this, MyQuestionsActivity::class.java)
        startActivity(intent)
    }
}
