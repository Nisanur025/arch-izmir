package com.example.arch_izmir.data.model

import androidx.annotation.DrawableRes

data class Place(
    val id: Int,
    val name: String,
    val district : String, // Semt Bilgisi
    @DrawableRes val imageRes: Int,
    val isFavorite : Boolean = false, // Favorilere Ekleme
    val isAddedToRoad :Boolean = false // rotaya ekleme
)
