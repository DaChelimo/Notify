@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.activity

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ramadan.notify.R
import com.ramadan.notify.databinding.WhiteboardBinding
import com.ramadan.notify.ui.viewModel.NoteListener
import com.ramadan.notify.ui.viewModel.WhiteboardViewModel
import com.ramadan.notify.utils.TouchListener
import com.ramadan.notify.utils.startHomeActivity
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import com.yalantis.contextmenu.lib.MenuObject
import com.yalantis.contextmenu.lib.MenuParams
import kotlinx.android.synthetic.main.whiteboard.*


class Whiteboard : AppCompatActivity(), NoteListener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(WhiteboardViewModel::class.java)
    }
    private lateinit var binding: WhiteboardBinding
    private lateinit var contextMenuDialogFragment: ContextMenuDialogFragment
    private var whiteboardName: String = "null"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Whiteboard"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.whiteboard)
        binding.whiteboardModel = viewModel
        binding.lifecycleOwner = this
        whiteboard.requestFocus()
        whiteboard.setOnTouchListener(TouchListener())
        initMenuFragment()

        penColorPicker.setListener { position, color ->
            whiteboard.setCurrentWidth(seekBar.progress)
            whiteboard.setCurrentColor(color)
            eraser.setBackgroundColor(resources.getColor(R.color.white))
        }

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                whiteboard.setCurrentWidth(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    override fun onBackPressed() {
        if (whiteboard.isDirty) {
            println("Dirty")
        }
        showAlertDialog()
    }

    private fun showAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.alert_dialog, null)
        dialogBuilder.setView(view)
        dialogBuilder.setCancelable(true)
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        val saveChange = view.findViewById<TextView>(R.id.saveChange)
        val dismiss = view.findViewById<TextView>(R.id.dismiss)
        saveChange.setOnClickListener {
            setName()
            alertDialog.cancel()
        }
        dismiss.setOnClickListener { super.onBackPressed() }
    }

    private fun setName() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.rename_dialog, null)
        dialogBuilder.setView(view)
        dialogBuilder.setCancelable(true)
        val fileName = view.findViewById<View>(R.id.new_name) as EditText
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        val confirm = view.findViewById<TextView>(R.id.confirm)
        val cancel = view.findViewById<TextView>(R.id.cancel)
        confirm.setOnClickListener {
            whiteboardName = fileName.text.toString()
            viewModel.saveDrawingNote(whiteboardName, this, whiteboard)
            super.onBackPressed()
        }
        cancel.setOnClickListener { alertDialog.cancel() }
    }

    fun eraser(view: View) {
        whiteboard.setCurrentWidth(seekBar.progress * 8)
        whiteboard.setCurrentColor(Color.WHITE)
        eraser.setBackgroundColor(resources.getColor(R.color.colorAccent))
        penColorPicker.isLockMode = true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.context_menu -> {
                    showContextMenuDialogFragment()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initMenuFragment() {
        val menuParams = MenuParams(
            actionBarSize = resources.getDimension(R.dimen.tool_bar_height).toInt(),
            menuObjects = getMenuObjects(),
            isClosableOutside = true
        )

        contextMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams).apply {
            menuItemClickListener = { view, position ->
                when (position) {
                    0 -> {
                        viewModel.clearDrawingNote(this@Whiteboard.whiteboard)
                    }
                    1 -> {
                        setName()
                    }
                }
            }
        }
    }

    private fun getMenuObjects() = mutableListOf<MenuObject>().apply {
        val clear =
            MenuObject("Clear").apply { setResourceValue(R.drawable.clear_whiteboard) }
        clear.setBgColorValue((Color.rgb(238, 238, 238)))
        val save =
            MenuObject("Save").apply { setResourceValue(R.drawable.save_note) }
        save.setBgColorValue((Color.WHITE))
        add(clear)
        add(save)
    }

    private fun showContextMenuDialogFragment() {
        if (supportFragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
            contextMenuDialogFragment.show(supportFragmentManager, ContextMenuDialogFragment.TAG)
        }
    }

    override fun onStarted() {
        whiteboardProgress.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        whiteboardProgress.visibility = View.GONE
        startHomeActivity()
    }

    override fun onFailure(message: String) {
        whiteboardProgress.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}