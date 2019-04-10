package org.riseintime.ziiq

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_my_questions.*
import org.jetbrains.anko.startActivity
import org.riseintime.ziiq.model.Question
import org.riseintime.ziiq.recyclerview.item.QuestionItem
import java.util.*

class MyQuestionsActivity : AppCompatActivity() {

    private lateinit var questionSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_questions)
        loadQuestions()

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            openMyQuestionsActivity()
        }

    }

    private fun loadQuestions() {
        FirebaseFirestore.getInstance().collection("questions")
            .whereEqualTo(
                "user",
                FirebaseAuth.getInstance().uid ?: throw java.lang.NullPointerException("UID is null")
            ).whereEqualTo(
                "lang",
                Locale.getDefault().getLanguage()
            ).get()
            .addOnSuccessListener { result ->
                val items = mutableListOf<Item>()
                result!!.documents.forEach {
                    items.add(QuestionItem(it.toObject(Question::class.java)!!))
                }
                initRecyclerView(items)
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE", "Error getting documents: ", exception)
            }
    }

    private fun initRecyclerView(items: List<Item>) {
        recyclerview_questions.apply {
            layoutManager = LinearLayoutManager(this@MyQuestionsActivity.applicationContext)
            adapter = GroupAdapter<ViewHolder>().apply {
                questionSection = Section(items)
                add(questionSection)
                setOnItemClickListener(onItemClick)
            }
        }
    }

    fun openMyQuestionsActivity() {
        val intent = Intent(this, NewQuestionActivity::class.java)
        startActivity(intent)
    }

    private val onItemClick = OnItemClickListener { item, view ->
        if (item is QuestionItem) {
            startActivity<QuestionDetailActivity>(
                "question_id" to item.question.id
            )
        }
    }

}
