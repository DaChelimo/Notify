package com.ramadan.notify.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.devlomi.record_view.OnRecordListener
import com.ramadan.notify.R
import com.ramadan.notify.ui.viewModel.NoteListener
import com.ramadan.notify.ui.viewModel.RecordViewModel
import kotlinx.android.synthetic.main.record.*


class Record : AppCompatActivity(), NoteListener {
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 77

    private var isRecording = false

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RecordViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.record)
        supportActionBar?.title = "Voice note"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recordButton.isListenForRecord = true
//        recordButton.setOnRecordClickListener {
//            Toast.makeText(this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT)
//                .show()
//            Log.d("RecordButton", "RECORD BUTTON CLICKED")
//        }
        recordButton.setRecordView(recordView)
        recordView.setSoundEnabled(false)
        recordView.setLessThanSecondAllowed(false)
        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0)
        recordView.setSlideToCancelTextColor(Color.parseColor("#313131"))
        recordView.setSlideToCancelArrowColor(Color.parseColor("#313131"))
        recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                if (!isRecording) {
                    if (ContextCompat.checkSelfPermission(this@Record, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                            this@Record, arrayOf(Manifest.permission.RECORD_AUDIO), 77)
                    } else {
                        Handler().postDelayed(Runnable {
                            viewModel.startRecording()
                            viewModel.startChronometer(chronometer)
                            recordProgressBar.isIndeterminate = true
                        }, 1000)
                        //keep screen on while recording
                    }
                } else {
                    viewModel.stopRecording()
                    viewModel.stopChronometer(chronometer)
                    recordProgressBar.isIndeterminate = false

                }
            }

            override fun onCancel() {
                viewModel.stopRecording()
                viewModel.stopChronometer(chronometer)
                recordProgressBar.isIndeterminate = false
                Toast.makeText(this@Record, "Canceled", Toast.LENGTH_SHORT).show()
            }

            override fun onFinish(recordTime: Long) {
                viewModel.stopRecording()
                viewModel.stopChronometer(chronometer)
                recordProgressBar.isIndeterminate = false
                Toast.makeText(this@Record, "Saved", Toast.LENGTH_SHORT).show()
            }

            override fun onLessThanSecond() {
                viewModel.stopChronometer(chronometer)
                recordProgressBar.isIndeterminate = false
            }
        })
        recordView.setOnBasketAnimationEndListener {
            viewModel.stopChronometer(chronometer)
            recordProgressBar.isIndeterminate = false
        }

    }


    override fun onStarted() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

    override fun onFailure(message: String) {
        TODO("Not yet implemented")
    }


}