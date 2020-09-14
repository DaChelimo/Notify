@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.viewModel

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class WhiteboardViewModel : ViewModel() {
    var file: File? = null
    private val dirPath = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    ).path + "/Notify"
    var noteListener: NoteListener? = null

    fun saveImageToExternalStorage(bitmap: Bitmap, fileName: String) {
        try {
            val dir = File(dirPath)
            if (!dir.exists())
                dir.mkdirs()
            val file = File("$dirPath/$fileName.jpg")
            if (file.exists()) {
                noteListener?.onFailure("this name is already exist")
                return
            }
            file.createNewFile()
            val outStream: OutputStream?
            outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
            noteListener?.onSuccess()
        } catch (e: Exception) {
            Log.e("saveToExternalStorage()", e.message!!)
            noteListener?.onFailure(e.message!!)
            return
        }
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