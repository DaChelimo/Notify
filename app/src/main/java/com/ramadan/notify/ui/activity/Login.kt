package com.ramadan.notify.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ramadan.notify.R
import com.ramadan.notify.databinding.LoginBinding
import com.ramadan.notify.ui.viewModel.AuthListener
import com.ramadan.notify.ui.viewModel.AuthViewModel
import com.ramadan.notify.ui.viewModel.AuthViewModelFactory
import com.ramadan.notify.utils.startHomeActivity
import kotlinx.android.synthetic.main.login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class Login : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: LoginBinding = DataBindingUtil.setContentView(this, R.layout.login)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        binding.loginModel = viewModel
        binding.lifecycleOwner = this
        viewModel.authListener = this
        supportActionBar?.hide()


    }

    override fun onResume() {
        super.onResume()
        viewModel.user?.let { startHomeActivity() }
    }

    override fun onStarted() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressBar.visibility = View.GONE
        startHomeActivity()
    }

    override fun onFailure(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
