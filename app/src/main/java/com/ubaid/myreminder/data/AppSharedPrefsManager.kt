package com.ubaid.myreminder.data

import android.content.Context
import android.content.SharedPreferences
import com.ubaid.myreminder.R

object AppSharedPrefsManager {
    private lateinit var sharedPrefManager:SharedPreferences
    private const val SPLASH_VIEWED = "splash_viewed"
    fun saveSplashViewed(context: Context){
        sharedPrefManager = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        sharedPrefManager.edit().putBoolean(SPLASH_VIEWED, true).apply()
    }

    fun isSplashViewed(context: Context):Boolean {
        sharedPrefManager = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return sharedPrefManager.getBoolean(SPLASH_VIEWED, false)
    }

}