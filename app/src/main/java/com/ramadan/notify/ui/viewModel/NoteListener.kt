package com.ramadan.notify.ui.viewModel

interface NoteListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}