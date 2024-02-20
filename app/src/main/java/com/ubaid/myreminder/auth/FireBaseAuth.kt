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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.ubaid.myreminder.R
import com.ubaid.myreminder.ReminderViewModel

class FireBaseAuth {
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private var currentUser: FirebaseUser? = null
    lateinit var reminderViewModel: ReminderViewModel

    fun auth(context: Activity) {
        auth = Firebase.auth
        oneTapClient = Identity.getSignInClient(context)
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(context.getString(R.string.server_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener { result ->
            Log.d(FireBaseAuth.TAG, ">>Success ")
            ActivityCompat.startIntentSenderForResult(
                context,
                result.pendingIntent.intentSender, FireBaseAuth.REQ_CODE,
                null, 0, 0, 0, null
            )
        }.addOnFailureListener {
            Log.d(FireBaseAuth.TAG, "Failed123: " + it.message)

        }
    }

    fun result(activity: AppCompatActivity ,requestCode: Int, resultCode: Int, data: Intent?){
        reminderViewModel = ViewModelProvider(activity)[ReminderViewModel::class.java]
        auth = Firebase.auth
        oneTapClient = Identity.getSignInClient(activity)
        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        val idToken = googleCredential.googleIdToken
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            val user = auth.currentUser
                            Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show()
                            activity.supportFragmentManager.popBackStack()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
            else -> {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        }
    }


    companion object {
        val TAG = FireBaseAuth::class.java.simpleName
        val REQ_CODE = 123
    }

}

