@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.activity

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.github.naz013.colorslider.ColorSlider
import com.ramadan.notify.R
import com.ramadan.notify.databinding.NewWhiteboardBinding
import com.ramadan.notify.ui.viewModel.NoteListener
import com.ramadan.notify.ui.viewModel.NoteViewModel
import com.ramadan.notify.ui.viewModel.NoteViewModelFactory
import com.ramadan.notify.utils.TouchListener
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import com.yalantis.contextmenu.lib.MenuObject
import com.yalantis.contextmenu.lib.MenuParams
import kotlinx.android.synthetic.main.new_whiteboard.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class DrawNote : AppCompatActivity(), NoteListener, KodeinAware {
    override val kodein by kodein()
    private val factory: NoteViewModelFactory by instance()
    private val viewModel by lazy {
        ViewModelProviders.of(this, factory).get(NoteViewModel::class.java)
    }
    private lateinit var binding: NewWhiteboardBinding
    private lateinit var contextMenuDialogFragment: ContextMenuDialogFragment
    var dialogBuilder: AlertDialog.Builder? = null
    var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.new_whiteboard)
        binding.noteModel = viewModel
        binding.lifecycleOwner = this
        viewModel.noteListener = this
        whiteboard.requestFocus()
        whiteboard.setOnTouchListener(TouchListener())
        initMenuFragment()

        penColorPicker.setListener(ColorSlider.OnColorSelectedListener { position, color ->
            whiteboard.setCurrentWidth(seekBar.progress)
            whiteboard.setCurrentColor(color)
        })

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                whiteboard.setCurrentWidth(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


    }

    fun eraser(view: View) {
        whiteboard.setCurrentWidth(seekBar.progress * 6)
        whiteboard.setCurrentColor(Color.WHITE)
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
                    1 -> {
                        viewModel.clearDrawingNote(this@DrawNote.whiteboard)
                    }
                    2 -> {
                        viewModel.saveDrawingNote(view, this@DrawNote.whiteboard)
                    }
                    3 -> {
                    }
                }
            }
        }
    }

    private fun getMenuObjects() = mutableListOf<MenuObject>().apply {
        val empty = MenuObject().apply {}
        empty.setBgColorValue((Color.BLACK))
        val clear =
            MenuObject("Clear").apply { setResourceValue(R.drawable.new_whiteboard) }
        clear.setBgColorValue((Color.rgb(32, 32, 32)))
        val save =
            MenuObject("Save").apply { setResourceValue(R.drawable.save_note) }
        save.setBgColorValue((Color.BLACK))
        val delete =
            MenuObject("Delete").apply { setResourceValue(R.drawable.delete) }
        delete.setBgColorValue((Color.rgb(32, 32, 32)))
        add(empty)
        add(clear)
        add(save)
        add(delete)
    }

    private fun showContextMenuDialogFragment() {
        if (supportFragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
            contextMenuDialogFragment.show(supportFragmentManager, ContextMenuDialogFragment.TAG)
        }
    }

    override fun onStarted() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

    override fun onFailure(message: String) {
        TODO("Not yet implemented")
    }


}