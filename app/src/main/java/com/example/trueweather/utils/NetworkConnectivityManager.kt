package com.example.trueweather.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.example.trueweather.AppContextProvider
import javax.inject.Inject

//TODO: Migrate this to a Dagger injection class
@SuppressLint("StaticFieldLeak")
object NetworkConnectivityManager {
    private val context = AppContextProvider.getApplicationContext()

    fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}