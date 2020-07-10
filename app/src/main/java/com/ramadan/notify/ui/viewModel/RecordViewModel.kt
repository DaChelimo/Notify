@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.viewModel

import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.squti.androidwaverecorder.WaveRecorder
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class RecordViewModel : ViewModel() {

    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 77
    private var waveRecorder: WaveRecorder? = null
    private lateinit var filePath: String
    private var isRecording = false
    private var isPaused = false


    fun startRecording() {
        isRecording = true
        saveRecordToExternalStorage()
        waveRecorder?.startRecording()
//        noiseSuppressorSwitch.isEnabled = false
    }

    fun stopRecording() {
        isRecording = false
        waveRecorder?.stopRecording()
        println("File saved at : $filePath")
//        showAmplitudeSwitch.isChecked = false
//        Toast.makeText(this@RecordViewModel, "File saved at : $filePath", Toast.LENGTH_LONG).show()
//        noiseSuppressorSwitch.isEnabled = true
    }

//    private fun pauseRecording() {
//        isPaused = true
//        waveRecorder.pauseRecording()
//    }
//
//    private fun resumeRecording() {
//        isPaused = false
//        waveRecorder.resumeRecording()
//    }

    private fun saveRecordToExternalStorage() {
        filePath =
            Environment.getExternalStorageDirectory().absolutePath + "/Notify/Records/" + "notify" + System.currentTimeMillis()
                .toString() + ".wav"
        val dirPath = Environment.getExternalStorageDirectory().absolutePath.toString() + "/Notify/Records"
//        val file = "notify" + UUID.randomUUID().toString() + ".png"
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.TITLE, file)
//        values.put(MediaStore.Images.Media.CONTENT_TYPE, "path")
//        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
//        val uri: Uri =
//            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        try {
            val dir = File(dirPath)
            if (!dir.exists())
                dir.mkdirs()
            val outStream: OutputStream?
            val file = File(filePath)
            file.createNewFile()
            outStream = FileOutputStream(file)
            waveRecorder = WaveRecorder(filePath)
            outStream.flush() // empty the buffer
            outStream.close() // close the stream
        } catch (e: Exception) {
            Log.e("saveToExternalStorage()", e.message)
        }
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