package com.ramadan.notify.ui.activity

import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.ramadan.notify.R
import com.ramadan.notify.utils.startLoginActivity


class SplashScreen : AppCompatActivity() {
    private val timeOut: Long = 2500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        supportActionBar?.hide()
        Handler().postDelayed(Runnable {
            startLoginActivity()
        }, timeOut)
    }
}