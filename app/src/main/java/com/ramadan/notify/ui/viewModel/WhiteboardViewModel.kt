@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.viewModel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.ramadan.notify.utils.DrawView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class WhiteboardViewModel : ViewModel() {
    private lateinit var filePath: String
    private var FilePathStrings: Array<String?>? = null
    private var listFile: Array<File>? = null
    var file: File? = null
    private val dirPath = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    ).path.toString() + "/Notify"

    val noteListener: NoteListener? = null

    fun clearDrawingNote(whiteboard: DrawView) {
        whiteboard.clear()
    }


    fun saveDrawingNote(fileName: String, context: Context, whiteboard: DrawView) {
        val requestCode = 112
        if (Build.VERSION.SDK_INT >= 23) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (!hasPermissions(context, *permissions))
                ActivityCompat.requestPermissions((context as Activity), permissions, requestCode)
            whiteboard.isDrawingCacheEnabled = true
            saveImageToExternalStorage(whiteboard.drawingCache, fileName)
            whiteboard.destroyDrawingCache()
        } else {
            noteListener?.onFailure("Can't get permission")
        }
    }


    private fun saveImageToExternalStorage(bitmap: Bitmap, fileName: String) {
        filePath = "$dirPath/$fileName.jpg"
        print(filePath)
        try {
            val dir = File(dirPath)
            if (!dir.exists())
                dir.mkdirs()
            val file = File(filePath)
            if (file.exists()) {
                noteListener?.onFailure("Name is already exist")
                return
            }
            noteListener?.onStarted()
            file.createNewFile()
            val outStream: OutputStream?
            outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush()
            outStream.close()
            noteListener?.onSuccess()
        } catch (e: Exception) {
            Log.e("saveToExternalStorage()", e.message!!)
            noteListener?.onFailure(e.message!!)

        }
    }

    fun loadWhiteboards(): Array<String?>? {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED)
            noteListener?.onFailure("Failed to load whiteboards")
        else
            noteListener?.onStarted()
        file = File(dirPath)
        if (file!!.isDirectory) {
            listFile = file!!.listFiles()
            FilePathStrings = arrayOfNulls(listFile!!.size)
            for (i in listFile!!.indices) {
                FilePathStrings!![i] = listFile!![i].absolutePath
            }
        }
        noteListener?.onSuccess()
        return FilePathStrings
    }


    private fun hasPermissions(
        context: Context?,
        vararg permissions: String
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }


}