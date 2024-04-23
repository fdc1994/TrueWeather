package com.example.trueweather.utils

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.UiThread
import androidx.core.view.isGone

@UiThread
fun <V : View?> Activity.lazyFindViewById(@IdRes id: Int) =
    lazy(LazyThreadSafetyMode.NONE) { findViewById<V>(id) }

fun View.setGone(isGone: Boolean) {
    this.isGone = isGone
}

