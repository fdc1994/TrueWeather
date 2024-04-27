package com.example.network.data

import java.io.Serializable

data class UserPreferences(
    val locationsList: List<String>
): Serializable