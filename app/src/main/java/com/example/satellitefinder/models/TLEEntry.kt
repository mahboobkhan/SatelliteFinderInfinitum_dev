package com.example.satellitefinder.models

import androidx.annotation.Keep

@Keep
data class TLEEntry(
    val name: String,
    val line1: String,
    val line2: String
)