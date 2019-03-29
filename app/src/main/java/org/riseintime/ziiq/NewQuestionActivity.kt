package org.riseintime.ziiq

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_new_question.*
import kotlinx.android.synthetic.main.fragment_my_account.*
import org.riseintime.ziiq.model.Question
import org.riseintime.ziiq.model.User
import org.riseintime.ziiq.util.FirestoreUtil
import java.util.*

class NewQuestionActivity : AppCompatActivity() {

    private var correctAnswer: Int = 0
    private val newQuestionId: String by lazy {
        FirebaseFirestore.getInstance().collection(FirestoreKey.QUESTIONS).document().id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_question)
    }

    fun save(view: View) {

        if (correctAnswer == 0) {
            Toast.makeText(applicationContext, "Mark correct answer", Toast.LENGTH_SHORT).show()
            return
        }
        if (new_question_text.text.toString() == "" ||
            new_question_a1.text.toString() == "" ||
            new_question_a2.text.toString() == "" ||
            new_question_a3.text.toString() == "" ||
            new_question_a4.text.toString() == ""
        ) {
            Toast.makeText(applicationContext, "Fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val newQuestion = Question(
            newQuestionId,
            new_question_text.text.toString(),
            new_question_a1.text.toString(),
            new_question_a2.text.toString(),
            new_question_a3.text.toString(),
            new_question_a4.text.toString(),
            FirebaseAuth.getInstance().currentUser?.uid ?: "",
            Locale.getDefault().getLanguage() ,
            correctAnswer, (0..Int.MAX_VALUE).random(), 0, 0, 0, 0, 0, 0, 0
        )

        FirebaseFirestore.getInstance().collection("questions").document(newQuestionId).set(newQuestion)
            .addOnSuccessListener {
                new_question_text.text?.clear()
                new_question_a1.text.clear()
                new_question_a2.text.clear()
                new_question_a3.text.clear()
                new_question_a4.text.clear()
                Toast.makeText(applicationContext, "Added", Toast.LENGTH_SHORT).show()
            }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {

            when (view.getId()) {
                R.id.radioButton1 ->
                    correctAnswer = 1
                R.id.radioButton2 ->
                    correctAnswer = 2
                R.id.radioButton3 ->
                    correctAnswer = 3
                R.id.radioButton4 ->
                    correctAnswer = 4
            }
        }
    }
}
