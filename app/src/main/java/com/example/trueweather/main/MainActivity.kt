package com.example.trueweather.main

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.domain.data.managers.IpmaNetworkManager
import com.example.trueweather.R
import com.example.trueweather.utils.lazyFindViewById
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject
    lateinit var ipmaNetworkManager: IpmaNetworkManager

    private val textView by lazyFindViewById<TextView>(R.id.main_view)
    private val progressBar by lazyFindViewById<ProgressBar>(R.id.main_progress_bar)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        getBasicData()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getBasicData() {
        ipmaNetworkManager.getWeatherForecast("1131200")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                progressBar.isGone = false
            }
            .doOnError {
                textView.text = it.message
                progressBar.isGone = true
            }
            .doOnSuccess {
                textView.text = it.data.toString()
                progressBar.isGone = true
            }.subscribe()
    }
}