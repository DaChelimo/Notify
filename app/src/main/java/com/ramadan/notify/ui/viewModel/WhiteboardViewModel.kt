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
    var boolean_folder = false


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
            Log.d("file path", filePath)
        } else {
            println("22")
        }
    }


    private fun saveImageToExternalStorage(bitmap: Bitmap) {
        filePath =
            Environment.getExternalStorageDirectory().absolutePath + "/Notify" + "/Whiteboard/" + "notify" + System.currentTimeMillis()
                .toString() + ".jpg"
        val dirPath =
            Environment.getExternalStorageDirectory().absolutePath.toString() + "/Notify/Whiteboard"

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

    fun loadSavedImages(dir: File) {
        val folder = File(
            Environment.getExternalStorageDirectory().toString() + "/Notify/Whiteboard"
        )
    }

    fun loadImage(file: File?) {
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