@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.viewModel

import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import java.io.File


class RecordViewModel : ViewModel(), MediaRecorder.OnErrorListener {

    private var filePathStrings: Array<String?>? = null
    private var listFile: Array<File>? = null
    var file: File? = null
    var noteListener: NoteListener? = null

    private val dirPath = Environment.getExternalStorageDirectory().path + "/Notify/Records"
    private var filePath = dirPath + "/notify${System.currentTimeMillis()}.mp3"
    private var mRecorder = MediaRecorder()
    private var outputFile: File? = null

    fun startRecording() {
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
        mRecorder.setAudioEncodingBitRate(48000)
        mRecorder.setAudioSamplingRate(16000)
        mRecorder.setOutputFile(outputFile!!.path)
        mRecorder.setOnErrorListener(this)
        mRecorder.prepare()
        mRecorder.start()
        Log.w("Record", "started recording to " + outputFile)
    }

    fun stopRecording(boolean: Boolean) {
        mRecorder.setOnErrorListener(this)
        mRecorder.stop()
        mRecorder.release()
        if ((outputFile == null) || (boolean)) {
            Log.w("Record", "Deleted File ")
            outputFile?.delete()
        }
    }

    fun saveRecordToExternalStorage(fileName: String): Boolean {
        filePath = "$dirPath/$fileName.mp3"
        outputFile = File(filePath)
        if (outputFile!!.exists()) {
            return false
        }
        return true
    }


    fun loadRecords(): Array<String?>? {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            Log.e("Record", "Environment.MEDIA IS MOUNTED")
        } else {
            val dir = File(dirPath)
            if (!dir.exists())
                dir.mkdirs()
            file = File(dirPath)
        }
        if (file!!.isDirectory) {
            listFile = file!!.listFiles()
            listFile!!.sortByDescending { it.lastModified() }
            filePathStrings = arrayOfNulls(listFile!!.size)
            for (i in listFile!!.indices) {
                filePathStrings!![i] = listFile!![i].absolutePath
            }
        } else {
            Log.e("Records", "Error in load records2")
        }
        return filePathStrings
    }


    override fun onError(mr: MediaRecorder?, what: Int, extra: Int) {
        Log.e("Record", mr.toString())
//        recordListener!!.onFailure("sorry, try again")
    }

}