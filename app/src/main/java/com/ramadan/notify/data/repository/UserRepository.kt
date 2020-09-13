package com.ramadan.notify.data.repository

import androidx.lifecycle.LiveData
import com.ramadan.notify.data.model.WrittenNote

class UserRepository(private val repository: Repository) {
    fun login(email: String, password: String) = repository.login(email, password)
    suspend fun _login(email: String, password: String) = repository.facebookLogin(email, password)

    fun register(email: String, password: String) = repository.register(email, password)
    fun resetPassword(email: String) = repository.resetPassword(email)

    fun currentUser() = repository.currentUser()

    fun logout() = repository.logout()

}