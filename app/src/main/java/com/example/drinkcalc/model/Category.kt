package com.example.drinkcalc.model

data class Category(
    val name: String = "",
    val totalCost: UInt = 0u,
    val towarisches: MutableSet<String>,
)
