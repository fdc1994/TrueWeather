package com.example.trueweather

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domain.data.WeatherForecast
import com.example.domain.data.managers.IpmaNetworkManager
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var ipmaRepository: IpmaNetworkManager

    private lateinit var textView: TextView
    private lateinit var refreshButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.main_text_view)
        refreshButton = findViewById(R.id.refresh_button)

        refreshButton.setOnClickListener {
            ipmaRepository.getWeatherForecast("1010500")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { (_, _, data): WeatherForecast ->
                        textView.text = data.toString()
                    }
                )
                { throwable: Throwable ->
                    textView.text = throwable.message

                    Log.e("TAG", "Error: ", throwable)
                }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }
}