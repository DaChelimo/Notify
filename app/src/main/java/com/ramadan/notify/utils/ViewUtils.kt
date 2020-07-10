package com.ramadan.notify.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.WindowInsets
import com.ramadan.notify.data.model.Whiteboard
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.ui.activity.*

fun Context.startHomeActivity() =
    Intent(this, Home::class.java).also {
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

fun Context.startTestActivity() =
    Intent(this, Test::class.java).also {
        startActivity(it)
    }

fun Context.startWhiteboardActivity(whiteboard: Whiteboard) =
    Intent(this, com.ramadan.notify.ui.activity.Whiteboard::class.java).also {
        it.putExtra("path", whiteboard)
        startActivity(it)
    }

fun Context.startNewNoteActivity(writtenNote: WrittenNote) =
    Intent(this, NewNote::class.java).also {
        it.putExtra("note", writtenNote)
        startActivity(it)
    }


internal fun View.colorAnimation(from: Int, to: Int) {
    ValueAnimator.ofObject(ArgbEvaluator(), from, to).apply {
        duration = 300
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