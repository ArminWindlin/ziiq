package org.riseintime.ziiq

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.view.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import org.riseintime.ziiq.util.FirestoreUtil

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onStart() {
        super.onStart()
        Log.d("DEBUG", "STARTED SETTINGS ACTIVITY")
        FirestoreUtil.getCurrentUser { user ->
            editText_name.setText(user.name)
            editText_bio.setText(user.bio)
        }
    }

    fun save(view: View) {
        FirestoreUtil.updateCurrentUser(editText_name.text.toString(), editText_bio.text.toString())
    }

    fun signOut(view: View) {
        AuthUI.getInstance()
            .signOut(this@SettingsActivity.applicationContext!!)
            .addOnCompleteListener {
                startActivity(intentFor<SignInActivity>().newTask().clearTask())
            }
    }
}
