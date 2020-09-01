@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.activity

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroFragment.Companion.newInstance
import com.github.appintro.AppIntroPageTransformerType
import com.github.appintro.model.SliderPage
import com.ramadan.notify.R
import com.ramadan.notify.utils.startHomeActivity


class Test2 : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(
            AppIntroFragment.newInstance(
                "Welcome!",
                "This is a demo of the AppIntro library, using the second layout.",
                imageDrawable = R.drawable.back,
                backgroundColor = Color.parseColor("21bf73")
            )
        )

        addSlide(
            newInstance(
                SliderPage(
                    "Gradients!",
                    "This text is written on a gradient background",
                    imageDrawable = R.drawable.whiteboard,
                    descriptionColor = R.color.colorAccent,
                    backgroundColor = R.color.white

                )
            )
        )

        addSlide(
            newInstance(
                "Simple, yet Customizable",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                imageDrawable = R.drawable.dark_theme,
                descriptionColor = R.color.white,
                backgroundColor = R.color.colorAccent
            )
        )

        addSlide(
            newInstance(
                "Explore",
                "Feel free to explore the rest of the library demo!",
                imageDrawable = R.drawable.dark_theme,
                descriptionColor = R.color.colorAccent,
                backgroundColor = R.color.white
            )
        )

        isColorTransitionsEnabled = true
        setProgressIndicator()
        isVibrate = true
        vibrateDuration = 50L
        setImmersiveMode()
        setTransformer(AppIntroPageTransformerType.Parallax())
        askForPermissions(
            permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            slideNumber = 2,
            required = true
        )

    }

    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
//        super.onDonePressed(currentFragment)
        startHomeActivity()
    }
    override fun onUserDeniedPermission(permissionName: String) {
        println("Deny")
    }
    override fun onUserDisabledPermission(permissionName: String) {
        println("Don't ask again")
        // User pressed "Deny" + "Don't ask again" on the permission dialog
    }
}