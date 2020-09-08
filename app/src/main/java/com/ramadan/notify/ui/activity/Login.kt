@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.activity

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
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
    private val viewModel by lazy {
        ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: LoginBinding = DataBindingUtil.setContentView(this, R.layout.login)
        binding.loginModel = viewModel
        binding.lifecycleOwner = this
        viewModel.authListener = this
        supportActionBar?.hide()
        viewModel.user?.let { startHomeActivity() }
        forgot.setOnClickListener {
            emailDialog()
        }

    }


    private fun emailDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.edit_text_dialog, null)
        dialogBuilder.setView(view)
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window!!.attributes.windowAnimations = R.style.SlideAnimation
        alertDialog.show()
        val title = view.findViewById<TextView>(R.id.title)
        title.text = "Enter Your Email"
        val email = view.findViewById<View>(R.id.input) as EditText
        email.hint = "Email"
        val confirm = view.findViewById<TextView>(R.id.confirm)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        confirm.setOnClickListener {
            if (email.text.isNullOrEmpty())
                onFailure("Please enter your email")
            viewModel.resetPassword(email.text.toString())
            Toast.makeText(this, "Check your inbox", Toast.LENGTH_SHORT).show()
            alertDialog.cancel()
        }
        cancel.setOnClickListener { alertDialog.cancel() }
    }


    override fun onStart() {
        super.onStart()
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
