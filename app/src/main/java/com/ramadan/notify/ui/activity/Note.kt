@file:Suppress("CAST_NEVER_SUCCEEDS", "DEPRECATION")

package com.ramadan.notify.ui.activity

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ramadan.notify.R
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.databinding.NoteBinding
import com.ramadan.notify.ui.viewModel.NoteListener
import com.ramadan.notify.ui.viewModel.NoteViewModel
import com.ramadan.notify.ui.viewModel.NoteViewModelFactory
import com.ramadan.notify.utils.startHomeActivity
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import com.yalantis.contextmenu.lib.MenuObject
import com.yalantis.contextmenu.lib.MenuParams
import kotlinx.android.synthetic.main.note.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class Note : AppCompatActivity(), NoteListener, KodeinAware {

    override val kodein by kodein()
    private val factory: NoteViewModelFactory by instance()
    private val viewModel by lazy {
        ViewModelProviders.of(this, factory).get(NoteViewModel::class.java)
    }
    private lateinit var binding: NoteBinding
    private lateinit var contextMenuDialogFragment: ContextMenuDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "New note"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.note)
        binding.noteModel = viewModel
        binding.lifecycleOwner = this
        viewModel.noteListener = this
        noteColorPicker.setListener { position, color ->
            noteLayout.setBackgroundColor(color)
            viewModel.noteColor = color
        }
        initMenuFragment()
    }

    override fun onStart() {
        super.onStart()
        if (intent.hasExtra("note")) {
            val writtenNote: WrittenNote = intent.getSerializableExtra("note") as WrittenNote
            if (writtenNote.content.isNotEmpty()) {
                observeDate(writtenNote.ID)
            }
        }
    }

    override fun onBackPressed() {
        if (noteContent.text.isNullOrEmpty())
            super.onBackPressed()
        else
            showAlertDialog()
    }

    private fun showAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val layoutView = layoutInflater.inflate(R.layout.alert_dialog, null)
        dialogBuilder.setView(layoutView)
        val alertDialog = dialogBuilder.create()
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        val overwrite = layoutView.findViewById<TextView>(R.id.overwrite)
        val newNote = layoutView.findViewById<TextView>(R.id.newNote)
        overwrite.setOnClickListener { }
        newNote.setOnClickListener { }
    }

    private fun observeDate(ID: String) {
        viewModel.getNote(ID).observe(this, Observer {
            supportActionBar?.title = it.name
//            val colorDrawable =
//                ColorDrawable(ContextCompat.getColor(this, it.noteColor))
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it.noteColor))
            binding.noteModel = viewModel
            binding.lifecycleOwner = this
            viewModel.noteListener = this

        })
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
                        if (intent.hasExtra("note"))
                            viewModel.updateNote()
                        viewModel.insertNote()
                    }
                    1 -> {
                        viewModel.deleteNote()
                    }
                }
            }
        }
    }

    private fun getMenuObjects() = mutableListOf<MenuObject>().apply {
        val save =
            MenuObject("Save").apply { setResourceValue(R.drawable.save_note) }
        save.setBgColorValue((Color.rgb(238, 238, 238)))
        val delete =
            MenuObject("Delete").apply { setResourceValue(R.drawable.delete) }
        delete.setBgColorValue((Color.WHITE))
        add(save)
        add(delete)
    }

    private fun showContextMenuDialogFragment() {
        if (supportFragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
            contextMenuDialogFragment.show(supportFragmentManager, ContextMenuDialogFragment.TAG)
        }
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