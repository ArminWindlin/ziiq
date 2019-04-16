package org.riseintime.ziiq.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.riseintime.ziiq.model.User
import java.util.*

object FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val userId: String by lazy {
        FirebaseAuth.getInstance().uid ?: throw java.lang.NullPointerException("UID is null")
    }

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/$userId")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                    userId,
                    FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonymous" + (0..9999).random(),
                    0,
                    0,
                    Locale.getDefault().getLanguage()
                )
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else {
                onComplete()
            }
        }
    }

    fun updateCurrentUser(name: String = "") {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(User::class.java)!!)
            }
    }
}