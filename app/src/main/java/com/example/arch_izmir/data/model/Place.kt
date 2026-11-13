package com.example.arch_izmir.data.model

import androidx.annotation.DrawableRes

data class Place(
    val id: Int,
    val name: String,
    val district : String, // Semt Bilgisi
    @DrawableRes val imageRes: Int,
    var isFavorite : Boolean = false, // Favorilere Ekleme
    val isAddedToRoad :Boolean = false, // rotaya ekleme
    val description : String, // Detail sayfaasındaki mavi tarihçe kartı
    val audioGuideSubtitle: String? = null, // Detail sayfasındaki Sesli anlatım Alt Başlık, sesli anlatım olmayanlar için nullable
    val distanceKm: Double = 0.0,
    val latitude: Double = 38.4192,
    val longitude: Double = 27.1287,
    val points: Int = 0,            // Pasaport puanı (+10, +5)
    var isVisited: Boolean = false  // Gezildi mi?
)
