package com.example.trueweather.main

interface RetryListener {
    fun onLocationsRetry()
    fun onPermissionsRetry()
}