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
import com.ramadan.notify.utils.startHomeActivity
import kotlinx.android.synthetic.main.sign_up.*

class SignUp : AppCompatActivity(), AuthListener {
    //
//    override val kodein by kodein()
//    private val factory : AuthViewModelFactory by instance()
    private lateinit var binding: LoginBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)
        binding = DataBindingUtil.setContentView(this, R.layout.sign_up)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        viewModel.authListener = this
        supportActionBar?.hide()
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
