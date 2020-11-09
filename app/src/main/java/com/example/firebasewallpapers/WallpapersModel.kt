package com.example.firebasewallpapers

import com.google.firebase.Timestamp

data class WallpapersModel(
    val name: String = "",
    val image: String = "",
    val thumbnail: String = "",
    val date: Timestamp? = null
)