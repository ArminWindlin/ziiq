package org.riseintime.ziiq.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.riseintime.ziiq.model.User

object FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document(
            "users/${FirebaseAuth.getInstance().uid
                ?: throw java.lang.NullPointerException("UID is null")}"
        )

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                    FirebaseAuth.getInstance().currentUser?.displayName ?: "", 0,
                    "", 0
                )
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else {
                onComplete()
            }
        }
    }

    fun updateCurrentUser(name: String = "", bio: String = "") {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (name.isNotBlank()) userFieldMap["bio"] = bio
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(User::class.java)!!)
            }
    }
}