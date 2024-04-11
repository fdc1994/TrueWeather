package com.example.trueweather.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.trueweather.R
import com.example.trueweather.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }
    private val fakeTimer: Observable<Long> = Observable.timer(2, TimeUnit.SECONDS, Schedulers.io())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val startIntent = Intent(this, MainActivity::class.java)
        compositeDisposable.add(
            fakeTimer.doOnNext {
                startActivity(startIntent)
                finish()
            }.subscribe()
        )

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}