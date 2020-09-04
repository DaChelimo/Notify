package com.ramadan.notify.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ramadan.notify.R
import com.ramadan.notify.utils.startAppIntroActivity
import com.ramadan.notify.utils.startLoginActivity


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        supportActionBar?.hide()
        window.statusBarColor = Color.TRANSPARENT

        Handler().postDelayed(Runnable {
            startAppIntroActivity()
        }, 2000)
    }
}