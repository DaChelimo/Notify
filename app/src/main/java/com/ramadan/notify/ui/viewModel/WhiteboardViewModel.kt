@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.viewModel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import android.view.View
import android.widget.ImageView
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

    fun clearDrawingNote(whiteboard: DrawView) {
        whiteboard.clear()
    }


    fun saveDrawingNote(view: View, whiteboard: DrawView) {
        val mContext = view.context
        val requestCode = 112
        if (Build.VERSION.SDK_INT >= 23) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (!hasPermissions(mContext, *permissions))
                ActivityCompat.requestPermissions((mContext as Activity), permissions, requestCode)
            whiteboard.isDrawingCacheEnabled = true
            saveImageToExternalStorage(whiteboard.drawingCache)
            whiteboard.destroyDrawingCache()
        } else {
            println("22")
        }
    }


    private fun saveImageToExternalStorage(bitmap: Bitmap) {
        filePath =
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ).absolutePath + "/Notify/" + "notify" + System.currentTimeMillis()
                .toString() + ".jpg"
        val dirPath =
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ).absolutePath.toString() + "/Notify"

        try {
            val dir = File(dirPath)
            if (!dir.exists())
                dir.mkdirs()
//            val outStream: OutputStream = contentResolver.openOutputStream(uri)!!
            val outStream: OutputStream?
            val file = File(
                dirPath, "notify" + System.currentTimeMillis()
                    .toString() + ".jpg"
            )
            file.createNewFile()
            outStream = FileOutputStream(file)
            // 100 means no compression, the lower you go, the stronger the compression
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush() // empty the buffer
            outStream.close() // close the stream
        } catch (e: Exception) {
            Log.e("saveToExternalStorage()", e.message)
        }
    }

    fun loadWhiteboards(): Array<String?>? {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED
        ) {
            println("Error")
        } else {
            file = File(
                getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ).path + "/Notify"
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