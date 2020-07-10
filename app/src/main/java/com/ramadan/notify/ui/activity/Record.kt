@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.devlomi.record_view.OnRecordListener
import com.github.squti.androidwaverecorder.WaveRecorder
import com.ramadan.notify.R
import com.ramadan.notify.databinding.RecordBinding
import com.ramadan.notify.ui.viewModel.NoteListener
import com.ramadan.notify.ui.viewModel.RecordViewModel
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import kotlinx.android.synthetic.main.record.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Record : AppCompatActivity(), NoteListener {
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 77

    private lateinit var waveRecorder: WaveRecorder
    private var isRecording = false
    private var isPaused = false

    //    override val kodein by kodein()
//    private val factory: NoteViewModelFactory by instance()
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RecordViewModel::class.java)
    }

    private lateinit var binding: RecordBinding
    private lateinit var contextMenuDialogFragment: ContextMenuDialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.record)
        binding.recordModel = viewModel
        binding.lifecycleOwner = this
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        btnChangeOnclick.setOnClickListener(View.OnClickListener {
//            if (recordButton.isListenForRecord) {
//                recordButton.isListenForRecord = false
//                Toast.makeText(this, "onClickEnabled", Toast.LENGTH_SHORT).show()
//            } else {
//                recordButton.isListenForRecord = true
//                Toast.makeText(this, "onClickDisabled", Toast.LENGTH_SHORT).show()
//            }
//        })
        recordButton.isListenForRecord = true
        //ListenForRecord must be false ,otherwise onClick will not be called
        recordButton.setOnRecordClickListener {
            Toast.makeText(this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT)
                .show()
            Log.d("RecordButton", "RECORD BUTTON CLICKED")
        }

        recordButton.setRecordView(recordView);
        recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                //Start Recording..
                Log.d("RecordView", "onStart")
                if (!isRecording) {
                    if (ContextCompat.checkSelfPermission(
                            this@Record,
                            Manifest.permission.RECORD_AUDIO
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@Record,
                            arrayOf(Manifest.permission.RECORD_AUDIO),
                            PERMISSIONS_REQUEST_RECORD_AUDIO
                        )
                    } else {
                        viewModel.startRecording()
                    }
                } else {
                    viewModel.stopRecording()
                }
            }

            override fun onCancel() {
                Log.d("RecordView", "onCancel")
            }

            override fun onFinish(recordTime: Long) {
                val time: String = getHumanTimeText(recordTime)
                Log.d("RecordView", "onFinish")
                Log.d("RecordTime", time)
                viewModel.stopRecording()
            }

            override fun onLessThanSecond() {
                Log.d("RecordView", "onLessThanSecond")
            }
        })
        //ListenForRecord must be false ,otherwise onClick will not be called
        recordButton.setOnRecordClickListener {
            Toast.makeText(this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT)
                .show()
            Log.d("RecordButton", "RECORD BUTTON CLICKED")
        }
        recordView.setOnBasketAnimationEndListener {
            Log.d(
                "RecordView",
                "Basket Animation Finished"
            )
        }
        recordView.cancelBounds = 8F
        recordView.setSmallMicColor(Color.parseColor("#c2185b"));
        recordView.setSlideToCancelText("Slide to cancel");
        recordView.setSoundEnabled(false);
        recordView.setLessThanSecondAllowed(false);
        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);
        recordView.setSlideToCancelTextColor(Color.parseColor("#ff0000"));
        recordView.setSlideToCancelArrowColor(Color.parseColor("#ff0000"));
        recordView.setCounterTimeColor(Color.parseColor("#ff0000"));


        showAmplitudeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                amplitudeTextView.text = "Amplitude : 0"
                amplitudeTextView.visibility = View.VISIBLE
                waveRecorder.onAmplitudeListener = {
                    GlobalScope.launch(Dispatchers.Main) {
                        amplitudeTextView.text = "Amplitude : $it"
                    }
                }

            } else {
                waveRecorder.onAmplitudeListener = null
                amplitudeTextView.visibility = View.GONE
            }
        }

        noiseSuppressorSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            waveRecorder.noiseSuppressorActive = isChecked
            if (isChecked)
                Toast.makeText(this, "Noise Suppressor Activated", Toast.LENGTH_SHORT).show()

        }
    }

    private fun getHumanTimeText(milliseconds: Long): String {
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