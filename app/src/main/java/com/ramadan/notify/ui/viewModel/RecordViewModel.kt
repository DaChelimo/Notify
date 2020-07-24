@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.viewModel

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import androidx.lifecycle.ViewModel
import com.github.squti.androidwaverecorder.WaveRecorder
import com.ramadan.notify.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class RecordViewModel : ViewModel() {

    private var FilePathStrings: Array<String?>? = null
    private var listFile: Array<File>? = null
    var file: File? = null

    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 77
    private var waveRecorder: WaveRecorder? = null
    private lateinit var filePath: String
    private var name: String? = null
    private val dirPath = Environment.getExternalStorageDirectory().path + "/Notify/Records"


    fun startRecording() {
        saveRecordToExternalStorage()
        waveRecorder?.startRecording()

    }


    fun stopRecording() {
        waveRecorder?.stopRecording()

    }

    private fun saveRecordToExternalStorage() {
        filePath = "$dirPath/notify" + System.currentTimeMillis().toString() + ".mp3"
        try {
            val dir = File(dirPath)
            if (!dir.exists())
                dir.mkdirs()
            val outStream: OutputStream?
            val file = File(filePath)
            file.createNewFile()
            outStream = FileOutputStream(file)
            waveRecorder = WaveRecorder(filePath)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            Log.e("saveToExternalStorage()", e.message)
        }
    }

    fun startChronometer(chronometer: Chronometer) {
        chronometer.start()
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.setTextColor(Color.parseColor("#21bf73"))
    }

    fun stopChronometer(chronometer: Chronometer) {
        chronometer.stop()
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.setTextColor(Color.parseColor("#525252"))
    }


    fun loadRecords(): Array<String?>? {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED
        ) {
            println("Error")
        } else {
            file = File(
                Environment.getExternalStorageDirectory().absolutePath.toString() + "/Notify/Records"
            )
        }
        if (file!!.isDirectory) {
            listFile = file!!.listFiles()
            FilePathStrings = arrayOfNulls(listFile!!.size)
            for (i in listFile!!.indices) {
                FilePathStrings!![i] = listFile!![i].absolutePath
            }
        }
        return FilePathStrings
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_RECORD_AUDIO -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startRecording()
                }
                return
            }

            else -> {
            }
        }


    }
}