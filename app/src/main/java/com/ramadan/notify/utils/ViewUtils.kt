package com.ramadan.notify.utils

import android.content.Context
import android.content.Intent
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.ui.activity.Home
import com.ramadan.notify.ui.activity.Login
import com.ramadan.notify.ui.activity.NewNote

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

fun Context.startNewNoteActivity(writtenNote: WrittenNote) =
    Intent(this, NewNote::class.java).also {
        it.putExtra("note", writtenNote)
        startActivity(it)
    }