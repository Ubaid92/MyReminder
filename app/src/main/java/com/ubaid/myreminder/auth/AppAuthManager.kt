package com.ubaid.myreminder.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.ubaid.myreminder.R
import com.ubaid.myreminder.ReminderViewModel

class AppAuthManager {
    private var auth = Firebase.auth
    private lateinit var oneTapClient: SignInClient
    private lateinit var reminderViewModel: ReminderViewModel

    fun googleSignIn(context: Activity) {
        oneTapClient = Identity.getSignInClient(context)
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.server_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener { result ->
            Log.d(TAG, ">>Success ")
            ActivityCompat.startIntentSenderForResult(
                context,
                result.pendingIntent.intentSender, AppAuthManager.REQ_CODE,
                null, 0, 0, 0, null
            )
        }.addOnFailureListener {
            Log.d(TAG, "Failed123: " + it.message)

        }
    }

    fun result(activity: AppCompatActivity, requestCode: Int, resultCode: Int, data: Intent?) {
        reminderViewModel = ViewModelProvider(activity)[ReminderViewModel::class.java]
        oneTapClient = Identity.getSignInClient(activity)
        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = googleCredential.googleIdToken
        when {
            idToken != null -> {
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithCredential:success")
                            Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show()
                            activity.supportFragmentManager.popBackStack()

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }

            else -> {
                Log.d(TAG, "No ID token!")
            }
        }
    }

    fun isLoggedIn() = auth.currentUser != null

    fun signOut() {
        auth.signOut()
    }


    companion object {
        val TAG: String = AppAuthManager::class.java.simpleName
        const val REQ_CODE = 123
    }

}

