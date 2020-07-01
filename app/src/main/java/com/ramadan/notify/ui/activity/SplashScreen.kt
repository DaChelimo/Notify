package com.ramadan.notify.ui.activity

import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.ramadan.notify.R
import com.ramadan.notify.utils.startHomeActivity
import com.ramadan.notify.utils.startLoginActivity


class SplashScreen : AppCompatActivity() {
    private val timeOut: Long = 2500
    private lateinit var l1: LinearLayout
    private lateinit var l2: LinearLayout
    private lateinit var leftToRight: Animation
    private lateinit var rightToLeft: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        supportActionBar?.hide()
        Handler().postDelayed(Runnable {
        startLoginActivity()
        }, timeOut)

        l1 = findViewById(R.id.left)
        l2 = findViewById(R.id.right)
        leftToRight = AnimationUtils.loadAnimation(this, R.anim.left_to_right)
        rightToLeft = AnimationUtils.loadAnimation(this, R.anim.right_to_left)
        l1.animation = leftToRight
        l2.animation = rightToLeft
    }
}