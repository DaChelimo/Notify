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
    var noteColor: Int? = Color.parseColor("#ffffff")

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



    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}