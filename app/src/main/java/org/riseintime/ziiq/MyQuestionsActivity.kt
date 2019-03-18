package org.riseintime.ziiq

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_my_account.*
import org.riseintime.ziiq.util.FirestoreUtil

class MyQuestionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_questions)

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            openMyQuestionsActivity()
        }
    }

    fun openMyQuestionsActivity() {
        val intent = Intent(this, NewQuestionActivity::class.java)
        startActivity(intent)
    }

}
