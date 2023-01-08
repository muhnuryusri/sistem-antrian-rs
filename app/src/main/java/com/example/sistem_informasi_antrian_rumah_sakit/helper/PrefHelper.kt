package com.example.sistem_informasi_antrian_rumah_sakit.helper

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(context: Context) {
    private val PREF_NAME = "SharedPreferences"

    val KEY_IS_LOGIN = "KEY_IS_LOGIN"
    val KEY_USERNAME = "KEY_USERNAME"
    val KEY_PASSWORD = "KEY_PASSWORD"

    var pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = pref.edit()

    fun setLogin(isLogin: Boolean) {
        editor.putBoolean(KEY_IS_LOGIN, isLogin)
        editor.commit()
    }

    fun setUsername(username: String) {
        editor.putString(KEY_USERNAME, username)
        editor.commit()
    }

    fun setPassword(password: String) {
        editor.putString(KEY_PASSWORD, password)
        editor.commit()
    }

    fun isLogin(): Boolean {
        return pref.getBoolean(KEY_IS_LOGIN, false)
    }

    fun getUsername(): String? {
        return pref.getString(KEY_USERNAME, "")
    }

    fun getPassword(): String? {
        return pref.getString(KEY_PASSWORD, "")
    }

    fun removeData() {
        editor.clear()
        editor.commit()
    }
}