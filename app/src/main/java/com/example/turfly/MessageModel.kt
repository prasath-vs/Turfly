package com.example.turfly

import android.net.Uri

data class MessageModel(
    val message : String,
    val role : String,
    val imageUri: Uri? = null
)