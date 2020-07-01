@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.naz013.colorslider.ColorSlider
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.data.repository.NoteRepository
import com.ramadan.notify.utils.DrawView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    @SuppressLint("SimpleDateFormat")
    private val currentDate: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    private val todayDate: Date = Date()
    var ID: String? = System.currentTimeMillis().toString()
    var date: String? = currentDate.format(todayDate)
    var name: String? = null
    var content: String? = null
    var noteColor: Int? = Color.parseColor("#cacabf")

    var noteListener: NoteListener? = null
    private val disposables = CompositeDisposable()

    fun changeColor(colorSlider: ColorSlider) {
        println("Fffff")
        colorSlider.setListener { position, color ->
            println(color.toString())
            noteColor = color
        }
    }

    fun getNote(ID: String): MutableLiveData<WrittenNote> {
        val mutableData = MutableLiveData<WrittenNote>()
        repository.fetchNote(ID).observeForever {
            mutableData.value = it
            this.ID = it.ID
            name = it.name
            date = it.date
            content = it.content
            noteColor = it.noteColor
        }
        return mutableData
    }

    fun insertNote() {
        if (content.isNullOrEmpty()) {
            noteListener?.onFailure("No content to save")
            return
        }
        if (name.isNullOrEmpty())
            name = ""
        noteListener?.onStarted()
        val note = hashMapOf(
            "noteID" to ID, "noteDate" to date, "noteName" to name
            , "noteContent" to content, "noteColor" to noteColor
        )
        val disposable = repository.insertNote(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ noteListener?.onSuccess() }, { noteListener?.onFailure(it.message!!) })
        disposables.add(disposable)
    }

    fun updateNote() {
        if (name.isNullOrEmpty())
            name = ""
        noteListener?.onStarted()
        val note = hashMapOf(
            "noteID" to ID, "noteDate" to date, "noteName" to name
            , "noteContent" to content, "noteColor" to noteColor
        )
        val disposable = repository.updateNote(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ noteListener?.onSuccess() }, { noteListener?.onFailure(it.message!!) })
        disposables.add(disposable)
    }

    fun deleteNote() {
        if (content.isNullOrEmpty()) {
            noteListener?.onFailure("No content to delete")
            return
        }
        noteListener?.onStarted()
        val disposable = repository.deleteNote(ID!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ noteListener?.onSuccess() }, { noteListener?.onFailure(it.message!!) })
        disposables.add(disposable)
    }

    fun clearDrawingNote(whiteboard: DrawView) {
        whiteboard.clear()
    }

    fun saveDrawingNote(view: View, whiteboard: DrawView) {
        val mContext = view.context
        val REQUEST = 112
        if (Build.VERSION.SDK_INT >= 23) {
            val PERMISSIONS = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            if (!hasPermissions(mContext, *PERMISSIONS))
                ActivityCompat.requestPermissions((mContext as Activity), PERMISSIONS, REQUEST)
            whiteboard.isDrawingCacheEnabled = true
            saveImageToExternalStorage(whiteboard.drawingCache)
            whiteboard.destroyDrawingCache()
        } else {
            println("22")
        }
    }

    private fun hasPermissions(
        context: Context?,
        vararg permissions: String
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
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


    private fun saveImageToExternalStorage(bitmap: Bitmap) {
        val path = Environment.getExternalStorageDirectory().absolutePath.toString() + "/Notify"
//        val file = "notify" + UUID.randomUUID().toString() + ".png"
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.TITLE, file)
//        values.put(MediaStore.Images.Media.CONTENT_TYPE, "path")
//        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
//        val uri: Uri =
//            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        try {
            val dir = File(path)
            if (!dir.exists())
                dir.mkdirs()
//            val outStream: OutputStream = contentResolver.openOutputStream(uri)!!
            val outStream: OutputStream?
            val file = File(path, "notify" + System.currentTimeMillis().toString() + ".jpg")
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

    fun getDrawingNote() {
        val folder = File(
            Environment.getExternalStorageDirectory().toString() + "/Folder Name/"
        )

//        if (folder.exists())
//        val allFiles: Array<File> = folder.listFiles(FilenameFilter(function = ))

    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}