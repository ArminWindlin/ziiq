package org.riseintime.ziiq

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.riseintime.ziiq.adapter.RankingAdapter
import org.riseintime.ziiq.model.User

class RankingActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        loadUsers()
    }

    private fun loadUsers() {
        FirebaseFirestore.getInstance().collection("users")
            .orderBy("points", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { result ->
                val users = result.toObjects(User::class.java)
                initRecyclerView(users)
                val spinner = findViewById<View>(R.id.spinner_ranking)
                spinner.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                Log.d("FIREBASE", "Error getting documents: ", exception)
            }
    }

    private fun initRecyclerView(users: List<User>) {
        viewManager = LinearLayoutManager(this)
        viewAdapter = RankingAdapter(users)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view_ranking).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            adapter = viewAdapter
        }
    }
}
