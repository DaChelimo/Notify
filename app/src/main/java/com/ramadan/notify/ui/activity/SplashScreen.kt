package com.ramadan.notify.ui.activity

import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.ramadan.notify.R
import com.ramadan.notify.utils.startAppIntroActivity
import kotlinx.android.synthetic.main.splash_screen.*


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.navigationBarColor = getColor(R.color.transparent)
        supportActionBar?.hide()
        setContentView(R.layout.splash_screen)
        notifyLogo.animation = AnimationUtils.loadAnimation(
            this,
            R.anim.animate_in_out_enter
        )
        Handler().postDelayed(Runnable {
            startAppIntroActivity()
        }, 3000)
    }
}