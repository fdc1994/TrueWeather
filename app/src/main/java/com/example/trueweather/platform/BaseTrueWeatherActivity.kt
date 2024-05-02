package com.example.trueweather.platform

import android.Manifest
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
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


    fun requestPermissions(requestCode: Int) {
        ActivityCompat.requestPermissions(
            this as AppCompatActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            requestCode
        )
    }
}
