package com.example.mapboxapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class Splash : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var logoView: LottieAnimationView
    private val splash_Timeout: Int = 2000
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        titleTextView = findViewById(R.id.titleview)
        logoView = findViewById(R.id.logoview)
        animateText(titleTextView.text.toString())

        Handler(Looper.getMainLooper()).postDelayed({
            val splashIntent = Intent(this, SignIn::class.java)
            startActivity(splashIntent)
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            finish()
        }, splash_Timeout.toLong())

    }
    private fun animateText(text: String) {
        if (i <= text.length) {
            val fetchtext: String = text.substring(0, i);
            titleTextView.text = fetchtext
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    i++;
                    animateText(text)
                }, 100
            )
        }

    }
}