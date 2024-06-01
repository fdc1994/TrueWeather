package com.example.trueweather.utils

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.isGone
import androidx.viewbinding.ViewBinding

@UiThread
fun <V : View?> Activity.lazyFindViewById(@IdRes id: Int) =
    lazy(LazyThreadSafetyMode.NONE) { findViewById<V>(id) }

fun View.setGone(isGone: Boolean) {
    this.isGone = isGone
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}