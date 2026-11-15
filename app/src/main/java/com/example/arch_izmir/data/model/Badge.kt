package com.example.arch_izmir.data.model

enum class BadgeState {EARNED,LOCKED, IN_PROGRESS }
data class Badge(
    val name :String,
    val description :String,
    val state : BadgeState,
    val value: Int =0,
    val max : Int = 1,
    val dateText: String = ""
)
