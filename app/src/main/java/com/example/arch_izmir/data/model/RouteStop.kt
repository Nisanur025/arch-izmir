package com.example.arch_izmir.data.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "route_stops")
data class RouteStop(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title : String,
    val location : String,
    val visitDuration : String,
    val latitude: Double,
    val longitude: Double,
    val orderIndex : Int
)
