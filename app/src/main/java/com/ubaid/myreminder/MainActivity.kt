package com.ubaid.myreminder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.ubaid.myreminder.auth.FireBaseAuth
import com.ubaid.myreminder.databinding.ActivityMainBinding
import com.ubaid.myreminder.fragments.BaseFragment
import com.ubaid.myreminder.fragments.WelcomeScreenFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val fbAuth = FireBaseAuth()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replace(WelcomeScreenFragment())
        var oneTapClient = Identity.getSignInClient(this)
        var currentUser: FirebaseUser? = null
        var auth: FirebaseAuth = Firebase.auth
        currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(this, "Welcome back ${currentUser?.displayName}", Toast.LENGTH_SHORT)
                .show()
//            binding.heading.text = currentUser?.displayName.toString()

        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fbAuth.result(this, resultCode, resultCode, data)

    }

    fun replace(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    fun replaceAndAdd(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .addToBackStack(fragment::class.simpleName).commit()
    }

    fun add(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction().add(R.id.container, fragment)
            .addToBackStack(fragment::class.simpleName).commit()
    }

}
