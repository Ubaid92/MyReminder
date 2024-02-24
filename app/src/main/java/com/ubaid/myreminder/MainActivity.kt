package com.ubaid.myreminder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ubaid.myreminder.auth.AppAuthManager
import com.ubaid.myreminder.data.AppSharedPrefsManager
import com.ubaid.myreminder.databinding.ActivityMainBinding
import com.ubaid.myreminder.fragments.BaseFragment
import com.ubaid.myreminder.fragments.HomePageFragment
import com.ubaid.myreminder.fragments.WelcomeScreenFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val fbAuth = AppAuthManager()
    private val auth: FirebaseAuth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkSplash()
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        if (auth.currentUser != null) {
            Toast.makeText(
                this,
                "Welcome back ${auth.currentUser?.displayName}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkSplash() {
        if (AppSharedPrefsManager.isSplashViewed(this)) {
            replace(HomePageFragment())
        } else {
            replace(WelcomeScreenFragment())
            AppSharedPrefsManager.saveSplashViewed(this)
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
