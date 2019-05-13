package com.dev.lyhoangvinh.mvparchitecture.database

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.dev.lyhoangvinh.mvparchitecture.BuildConfig
import com.google.gson.Gson

/**
 * Created by LyHoangVinh on 11/5/2017.
 */

class SharedPrefs private constructor(application: Application) {
    private val mSharedPreferences: SharedPreferences

    init {
        mSharedPreferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private val PREFS_NAME = "share_prefs" + BuildConfig.APPLICATION_ID
        private var mInstance: SharedPrefs? = null

        fun getInstance(application: Application): SharedPrefs {
            if (mInstance == null) {
                mInstance = SharedPrefs(application)
            }
            return mInstance as SharedPrefs
        }
    }

    operator fun <T> get(key: String, anonymousClass: Class<T>): T {
        return when (anonymousClass) {
            String::class.java -> mSharedPreferences.getString(key, "") as T
            Boolean::class.java -> java.lang.Boolean.valueOf(mSharedPreferences.getBoolean(key, false)) as T
            Float::class.java -> java.lang.Float.valueOf(mSharedPreferences.getFloat(key, 0f)) as T
            Int::class.java -> Integer.valueOf(mSharedPreferences.getInt(key, 0)) as T
            Long::class.java -> java.lang.Long.valueOf(mSharedPreferences.getLong(key, 0)) as T
            else -> Gson().fromJson(mSharedPreferences.getString(key, ""), anonymousClass)
        }
    }

    fun <T> put(key: String, data: T) {
        val editor = mSharedPreferences.edit()
        when (data) {
            is String -> editor.putString(key, data as String)
            is Boolean -> editor.putBoolean(key, data as Boolean)
            is Float -> editor.putFloat(key, data as Float)
            is Int -> editor.putInt(key, data as Int)
            is Long -> editor.putLong(key, data as Long)
            else -> editor.putString(key, Gson().toJson(data))
        }
        editor.apply()
    }

    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }

    fun clearDataByKey(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }
}
