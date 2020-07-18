package com.ramadan.notify.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.WindowInsets
import com.ramadan.notify.MainActivity
import com.ramadan.notify.data.model.Record
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.ui.activity.Login
import com.ramadan.notify.ui.activity.Note
import com.ramadan.notify.ui.activity.Whiteboards

fun Context.startHomeActivity() =
    Intent(this, MainActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, Login::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startRecordActivity() =
    Intent(this, Record::class.java).also {
        startActivity(it)
    }

fun Context.startWhiteboardsActivity() =
    Intent(this, Whiteboards::class.java).also {
        startActivity(it)
    }

fun Context.startWhiteboardActivity(whiteboard: Record) =
    Intent(this, Record::class.java).also {
        it.putExtra("path", whiteboard)
        startActivity(it)
    }

fun Context.startNoteActivity(writtenNote: WrittenNote) =
    Intent(this, Note::class.java).also {
        it.putExtra("note", writtenNote)
        startActivity(it)
    }

fun Context.getRecordLength(milliseconds: Long): String {
    return String.format(
        "%02d:%02d",
        java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                java.util.concurrent.TimeUnit.MINUTES.toSeconds(
                    java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(
                        milliseconds
                    )
                )
    )

}


internal fun View.colorAnimation(from: Int, to: Int) {
    ValueAnimator.ofObject(ArgbEvaluator(), from, to).apply {
        duration = 400
        addUpdateListener { animator ->
            setBackgroundColor(animator.animatedValue as Int)
        }
    }.start()
}

internal fun View.applyWindowInsets(
    left: Boolean = false,
    top: Boolean = false,
    right: Boolean = false,
    bottom: Boolean = false
) {
    doOnApplyWindowInset { view, windowInsets, initialPadding ->
        val leftPadding = initialPadding.left +
                (windowInsets.systemWindowInsetLeft.takeIf { left } ?: 0)
        val topPaddin = initialPadding.top +
                (windowInsets.systemWindowInsetTop.takeIf { top } ?: 0)
        val rightPadding = initialPadding.right +
                (windowInsets.systemWindowInsetRight.takeIf { right } ?: 0)
        val bottomPadding = initialPadding.bottom +
                (windowInsets.systemWindowInsetBottom.takeIf { bottom } ?: 0)
        view.setPadding(leftPadding, topPaddin, rightPadding, bottomPadding)
    }
}

private fun View.doOnApplyWindowInset(f: (View, WindowInsets, InitialPadding) -> Unit) {
    val initialPadding = recordInitialPaddingForView(this)
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, insets, initialPadding)
        insets
    }

    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

private class InitialPadding(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft,
    view.paddingTop,
    view.paddingRight,
    view.paddingBottom
)