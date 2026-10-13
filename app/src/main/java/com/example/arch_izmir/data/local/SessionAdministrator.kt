package com.example.arch_izmir.data.local

import android.content.Context

class SessionAdministrator(context: Context) {

    private val prefs = context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_REMEMBER_ME = "remember_me"
        private const val KEY_EMAIL = "email"
    }

    // Girişten sonra çağrılır
    fun saveSession(remember: Boolean, email: String) {
        prefs.edit()
            .putBoolean(KEY_REMEMBER_ME, remember)
            .putString(KEY_EMAIL, if (remember) email else "")
            .apply()
    }

    fun isRememberChecked(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, false)

    fun userEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""

    // Çıkış yapınca çağrılır
    fun cikisYap() {
        prefs.edit().clear().apply()
    }
}