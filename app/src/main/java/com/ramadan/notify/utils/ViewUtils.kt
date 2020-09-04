package com.ramadan.notify.utils

import android.content.Context
import android.content.Intent
import com.ramadan.notify.MainActivity
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.ui.activity.AppIntro
import com.ramadan.notify.ui.activity.Login
import com.ramadan.notify.ui.activity.Note

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


fun Context.startAppIntroActivity() =
    Intent(this, AppIntro::class.java).also {
        startActivity(it)
    }

fun Context.startNoteActivity(writtenNote: WrittenNote) =
    Intent(this, Note::class.java).also {
        it.putExtra("note", writtenNote)
        startActivity(it)
    }

