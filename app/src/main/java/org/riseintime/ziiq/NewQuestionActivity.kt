package org.riseintime.ziiq

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_new_question.*
import kotlinx.android.synthetic.main.fragment_my_account.*
import org.riseintime.ziiq.model.Question
import org.riseintime.ziiq.model.User
import org.riseintime.ziiq.util.FirestoreUtil

class NewQuestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_question)
    }

    fun save(view: View) {
        val newQuestion = Question(
            new_question_text.text.toString(),
            new_question_a1.text.toString(),
            new_question_a2.text.toString(),
            new_question_a3.text.toString(),
            new_question_a4.text.toString(),
            FirebaseAuth.getInstance().currentUser?.uid ?: "",
            (0..Int.MAX_VALUE).random()
        )

        FirebaseFirestore.getInstance().collection("questions").document().set(newQuestion).addOnSuccessListener {
            new_question_text.text?.clear()
            Toast.makeText(applicationContext, "Added", Toast.LENGTH_SHORT).show()
        }
    }
}
