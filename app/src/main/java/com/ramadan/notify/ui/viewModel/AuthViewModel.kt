package com.ramadan.notify.ui.viewModel

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.ramadan.notify.MainActivity
import com.ramadan.notify.data.repository.UserRepository
import com.ramadan.notify.ui.activity.Login
import com.ramadan.notify.ui.activity.SignUp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    var email: String? = null
    var password: String? = null
    var confirmPassword: String? = null
    var authListener: AuthListener? = null
    private val disposables = CompositeDisposable()
    val user by lazy { repository.currentUser() }

    fun login() {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid email or password")
            return
        }
        authListener?.onStarted()
        val disposable = repository.login(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ authListener?.onSuccess() }, { authListener?.onFailure(it.message!!) })
        disposables.add(disposable)
    }

    fun signUp() {
        if (email.isNullOrEmpty() || password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()) {
            authListener?.onFailure("Please input all values")
            return
        }
        if (!password.equals(confirmPassword)) {
            authListener?.onFailure("Passwords don't match ")
            return
        }
        authListener?.onStarted()
        val disposable = repository.register(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ authListener?.onSuccess() }, { authListener?.onFailure(it.message!!) })
        disposables.add(disposable)
    }

    fun goToSignUp(view: View) {
        Intent(view.context, SignUp::class.java).also {
            view.context.startActivity(it)
        }
    }

    fun goToLogin(view: View) {
        Intent(view.context, Login::class.java).also {
            view.context.startActivity(it)
        }
    }
    fun goToX(view: View) {
        Intent(view.context, MainActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    //disposing the disposables
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}