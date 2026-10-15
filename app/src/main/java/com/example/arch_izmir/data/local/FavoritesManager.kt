package com.example.arch_izmir.data.local

import android.content.Context
import android.content.SharedPreferences

class FavoritesManager(context: Context, userEmail : String ) {
    // E-posta adresine özel bir dosya adı
    private val fileName = "favorites_prefs_${userEmail.hashCode()}"
    private val prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_FAVORITE_IDS = "favorite_ids"
    }

    // Favori ID'lerini Set olarak al
    fun getFavoriteIds(): MutableSet<String> {
        val storedSet = prefs.getStringSet(KEY_FAVORITE_IDS, mutableSetOf())
        return storedSet?.toMutableSet() ?: mutableSetOf()
    }

    // Favori ID'lerini kaydet
    fun saveFavoriteIds(favoriteIds: Set<String>) {
        prefs.edit().putStringSet(KEY_FAVORITE_IDS, favoriteIds).apply()
    }

}