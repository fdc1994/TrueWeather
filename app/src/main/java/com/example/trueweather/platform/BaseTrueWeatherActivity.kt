package com.example.trueweather.platform

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.trueweather.R
import com.example.trueweather.utils.lazyFindViewById
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseTrueWeatherActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    private lateinit var coordinatorLayout: CoordinatorLayout

    private val toolbar: Toolbar by lazy {
        coordinatorLayout.findViewById(R.id.toolbar)
    }

    private val progressBar : ProgressBar by lazyFindViewById<ProgressBar>(R.id.progress_bar)

    abstract fun handlePermissionResult(isGranted: Boolean): Unit

    override fun setContentView(layoutResID: Int) {
        coordinatorLayout = layoutInflater.inflate(R.layout.base_activity_layout, null) as CoordinatorLayout
        val activityContainer: FrameLayout = coordinatorLayout.findViewById(R.id.layout_container)
        layoutInflater.inflate(layoutResID, activityContainer, true)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        super.setContentView(coordinatorLayout)
    }

    fun subscribeDisposable(subscription: () -> Disposable) {
        compositeDisposable.add(subscription.invoke())
    }

    fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    protected fun setAppBarTitle(title: String) {
        toolbar.title = title
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    fun AppCompatActivity.requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Companion.LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission is already granted, handle accordingly
            // For example, you can perform location-related operations directly here
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               handlePermissionResult(true)
            } else {
                handlePermissionResult(false)
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
