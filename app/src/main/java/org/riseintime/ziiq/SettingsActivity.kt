package org.riseintime.ziiq

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.fragment_my_account.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
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
        }
    }

    fun save(view: View) {
        FirestoreUtil.updateCurrentUser(editText_name.text.toString())
        Toast.makeText(this, "Updated", Toast.LENGTH_LONG).show()
    }

    fun signOut(view: View) {
        AuthUI.getInstance()
            .signOut(this@SettingsActivity.applicationContext!!)
            .addOnCompleteListener {
                startActivity(intentFor<SignInActivity>().newTask().clearTask())
            }
    }

    fun viewPrivacyPolicy(view: View) {
        val uri = Uri.parse("https://arminwindlin.github.io/ziiq-pp/")
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}
