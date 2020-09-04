@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.devlomi.record_view.OnRecordListener
import com.ramadan.notify.R
import com.ramadan.notify.ui.viewModel.NoteListener
import com.ramadan.notify.ui.viewModel.RecordViewModel
import com.ramadan.notify.utils.startHomeActivity
import kotlinx.android.synthetic.main.record.*


class Record : AppCompatActivity(), NoteListener {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RecordViewModel::class.java)
    }
    var recordName = "notify${System.currentTimeMillis()}.mp3"

    override fun onResume() {
        super.onResume()
        Handler().postDelayed(Runnable { setName() }, 1500)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record)
        supportActionBar?.title = "Voice note"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recordButton.isListenForRecord = true
        recordButton.setRecordView(recordView)
        recordView.setSoundEnabled(false)
        recordView.setCounterTimeColor(Color.parseColor("#21bf73"))
        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0)
        recordView.setSlideToCancelTextColor(Color.parseColor("#21bf73"))
        recordView.setSlideToCancelArrowColor(Color.parseColor("#313131"))
        recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                if (ContextCompat.checkSelfPermission(
                        this@Record,
                        Manifest.permission.RECORD_AUDIO
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@Record, arrayOf(Manifest.permission.RECORD_AUDIO), 77
                    )
                }
                viewModel.startRecording()
                startTimer()
            }

            override fun onCancel() {
                viewModel.stopRecording(true)
                stopTimer()
                Toast.makeText(this@Record, "Canceled", Toast.LENGTH_SHORT).show()
            }

            override fun onFinish(recordTime: Long) {
                stopTimer()
                viewModel.stopRecording(false)
                Toast.makeText(this@Record, "Saved", Toast.LENGTH_SHORT).show()
                onSuccess()
            }

            override fun onLessThanSecond() {
                stopTimer()
            }
        })
        recordView.setOnBasketAnimationEndListener {
            stopTimer()
            Toast.makeText(this@Record, "Canceled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setName() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.rename_dialog, null)
        dialogBuilder.setView(view)
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "Voice note"
        val fileName = view.findViewById<View>(R.id.new_name) as EditText
        val confirm = view.findViewById<TextView>(R.id.confirm)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        confirm.setOnClickListener {
            this.recordName = fileName.text.toString()
            if (!viewModel.saveRecordToExternalStorage(recordName))
                onFailure("Sorry, record name is already exist")
            else
                alertDialog.cancel()
        }
        cancel.setOnClickListener { startHomeActivity() }
    }


    fun startTimer() {
        chronometer.start()
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.setTextColor(Color.parseColor("#21bf73"))
        recordProgressBar.isIndeterminate = true
    }

    fun stopTimer() {
        chronometer.stop()
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.setTextColor(Color.parseColor("#525252"))
        recordProgressBar.isIndeterminate = false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            77 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    viewModel.startRecording()
                }
                return
            }
            else -> {
            }
        }
    }

    override fun onStarted() {
    }


    override fun onSuccess() {
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        startHomeActivity()
    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun createNotification(): Notification? {
//        val mBuilder: NotificationCompat.Builder = GestureDescription.Builder(applicationContext)
//            .setSmallIcon(R.drawable.note)
//            .setContentTitle(getString(R.string.pause_recording_button))
//            .setContentText("mTimerFormat.format(mElapsedSeconds * 1000)")
//            .setOngoing(true)
//        mBuilder.setContentIntent(
//            PendingIntent.getActivities(
//                applicationContext, 0, arrayOf(
//                    Intent(
//                        applicationContext,
//                        MainActivity::class.java
//                    )
//                ), 0
//            )
//        )
//        return mBuilder.build()
//    }


}