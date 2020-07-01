package com.ramadan.notify.ui.viewModel

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}