package com.ramadan.notify.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.ramadan.notify.MainActivity
import com.ramadan.notify.R
import com.ramadan.notify.data.model.Record
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.ui.activity.*

fun Context.loadingDialog(): AlertDialog? = let {
    var alertDialog: AlertDialog? = null
    val dialogBuilder = AlertDialog.Builder(this)
    val layoutView = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
    dialogBuilder.setView(layoutView)
    alertDialog = dialogBuilder.create()
    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    return alertDialog
}

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

fun Context.startWhiteboardActivity() =
    Intent(this, Test2::class.java).also {
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
