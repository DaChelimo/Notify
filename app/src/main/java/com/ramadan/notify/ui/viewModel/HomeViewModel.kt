package com.ramadan.notify.ui.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.data.repository.NoteRepository
import com.ramadan.notify.utils.startLoginActivity


class HomeViewModel(private val repository: NoteRepository) : ViewModel() {
    fun getNotes(): LiveData<MutableList<WrittenNote>> {
        val mutableData = MutableLiveData<MutableList<WrittenNote>>()
        repository.fetchNotes().observeForever { mutableData.value = it }
        return mutableData
    }

    val user by lazy { repository.currentUser() }

    fun logout(view: View) {
        repository.logout()
        view.context.startLoginActivity()
    }

}