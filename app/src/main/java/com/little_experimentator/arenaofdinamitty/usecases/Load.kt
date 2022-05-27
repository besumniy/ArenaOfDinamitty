package com.little_experimentator.arenaofdinamitty.usecases

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.little_experimentator.arenaofdinamitty.MenuActivity

class Load(activity: Activity) {
    val activity=activity

    lateinit var pref: SharedPreferences

    fun getIsFistTime():Boolean{
        pref = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext())

        val editor = pref.edit()//delete later
        editor.putBoolean("isFirstTime", false)//maibe do it later and update system
        editor.apply()

        if (pref.contains("isFirstTime")) {
            return pref.getBoolean("isFirstTime", true)
        }
        return true
    }
    fun setIsFirstTime(){
        val editor = pref.edit()
        editor.putBoolean("isFirstTime", false)
        editor.apply()
    }


    fun isFinished(){
        activity.startActivity(Intent(activity, MenuActivity::class.java))
        activity.finish()


    }
}