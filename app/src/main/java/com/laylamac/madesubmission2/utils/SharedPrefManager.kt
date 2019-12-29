package com.laylamac.madesubmission2.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {

    private val SHARED_PREF_TITLE = "shared_preference_submission2"
    private val KEY_DAILY = "daily_reminder"
    private val KEY_RELEASE = "release_reminder"
    private val KEY_INIT = "init"
    private var mInstance : SharedPrefManager? = null
    private val sharePref : SharedPreferences? = context.getSharedPreferences(SHARED_PREF_TITLE, Context.MODE_PRIVATE)

    @Synchronized
    fun getInstance (context: Context): SharedPrefManager{
        if (mInstance == null){
            mInstance = SharedPrefManager(context)
        }
        return mInstance as SharedPrefManager
    }

    fun setDailyRemind(daily : Boolean){
        val editorDaily = sharePref?.edit()
        editorDaily?.putBoolean(KEY_DAILY, daily)
        editorDaily?.putInt(KEY_INIT, 1)
        editorDaily?.apply()
    }

    fun setReleaseRemind(release : Boolean){
        val editorRelease = sharePref?.edit()
        editorRelease?.putBoolean(KEY_RELEASE, release)
        editorRelease?.putInt(KEY_INIT, 1)
        editorRelease?.apply()
    }

    fun checkDailyReminder(): Boolean?{
        return sharePref?.getBoolean(KEY_DAILY, true)
    }

    fun checkReleaseReminder() : Boolean?{
        return sharePref?.getBoolean(KEY_RELEASE, true)
    }

    fun checkInit() : Int?{
        return sharePref?.getInt(KEY_INIT, 0)
    }

}
