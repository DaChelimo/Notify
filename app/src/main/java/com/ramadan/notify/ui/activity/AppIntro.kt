package com.ramadan.notify.ui.activity


import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.github.appintro.model.SliderPage
import com.ramadan.notify.R

class AppIntro : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        addSlide(
            AppIntroFragment.newInstance(
                SliderPage(
                    "Colorful Notes",
                    "Customization became much easier",
                    imageDrawable = R.drawable.notes,
                    backgroundColor = getColor(R.color.colorAccent)

                )
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                "Portable drawing board",
                "White and Black Boards",
                imageDrawable = R.drawable.drawing,
                titleColor = Color.parseColor("#21bf73"),
                descriptionColor = Color.parseColor("#21bf73"),
                backgroundColor = Color.parseColor("#ffffff")
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                SliderPage(
                    "Record and Listen",
                    " ",
                    imageDrawable = R.drawable.recording,
                    descriptionColor = Color.parseColor("#eeeeee"),
                    backgroundColor = Color.parseColor("#21bf73")

                )
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                "Day and Night Theme",
                " ",
                imageDrawable = R.drawable.daynight,
                titleColor = Color.parseColor("#21bf73"),
                descriptionColor = Color.parseColor("#21bf73"),
                backgroundColor = Color.parseColor("#313131")
            )
        )

        isColorTransitionsEnabled = true
        setProgressIndicator()
        setIndicatorColor(
            selectedIndicatorColor = getColor(R.color.colorPrimary),
            unselectedIndicatorColor = getColor(R.color.colorPrimaryDark)
        )
        isVibrate = true
        vibrateDuration = 50L
        setImmersiveMode()
        setTransformer(AppIntroPageTransformerType.Parallax())

        askForPermissions(
            permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            slideNumber = 1,
            required = true
        )
        askForPermissions(
            permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO
            ),
            slideNumber = 2,
            required = true
        )
    }

    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
//        startHomeActivity()
    }

    override fun onUserDeniedPermission(permissionName: String) {
        println("Deny")
        Toast.makeText(this, "MESSAGE", Toast.LENGTH_LONG).show()

    }

    override fun onUserDisabledPermission(permissionName: String) {
        println("Don't ask again")
        Toast.makeText(this, "MMMMM", Toast.LENGTH_LONG).show()

        // User pressed "Deny" + "Don't ask again" on the permission dialog
    }
//
//    override val isPolicyRespected: Boolean
//        get() = false
//
//    override fun onUserIllegallyRequestedNextPage() {
//        Toast.makeText(this, "MESSAGE", Toast.LENGTH_LONG).show()
//    }
}