package com.example.arch_izmir.data.local

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "users", // Tablo Adı
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long=0,
    val fullName : String,
    val email : String,
    val passwordHash : String,
    val createdAt :Long = System.currentTimeMillis()
)

