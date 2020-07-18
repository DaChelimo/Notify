@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.activity

import android.app.AlertDialog
import android.graphics.BitmapFactory
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
import com.ramadan.notify.R
import com.ramadan.notify.databinding.WhiteboardBinding
import com.ramadan.notify.ui.viewModel.NoteListener
import com.ramadan.notify.ui.viewModel.WhiteboardViewModel
import com.ramadan.notify.utils.TouchListener
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
    var dialogBuilder: AlertDialog.Builder? = null
    var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = ""
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.whiteboard)
        binding.whiteboardModel = viewModel
        binding.lifecycleOwner = this
//        viewModel.noteListener = this
        whiteboard.requestFocus()
        whiteboard.setOnTouchListener(TouchListener())
        initMenuFragment()

        penColorPicker.setListener { position, color ->
            whiteboard.setCurrentWidth(seekBar.progress)
            whiteboard.setCurrentColor(color)
        }

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                whiteboard.setCurrentWidth(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


    }

    override fun onStart() {
        super.onStart()
        if (intent.hasExtra("bitmap")) {
            val filepath = intent?.getStringArrayExtra("bitmap").toString()
            println(filepath)
            val bmpOptions = BitmapFactory.Options()
            bmpOptions.inSampleSize = 2
            bmpOptions.inJustDecodeBounds = false
            val bitmap = BitmapFactory.decodeFile(filepath, bmpOptions)
            println(bitmap?.toString())
            whiteboard.mBitmap = bitmap
//            println(intent?.getStringArrayExtra("bitmap").toString() + "   *****")
//            whiteboard.mBitmap = intent.getParcelableExtra("bitmap") as Bitmap

        }
    }

    fun eraser(view: View) {
        whiteboard.setCurrentWidth(seekBar.progress * 8)
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
                    0 -> {
                        viewModel.clearDrawingNote(this@Whiteboard.whiteboard)
                    }
                    1 -> {
                        viewModel.saveDrawingNote(view, this@Whiteboard.whiteboard)
                    }
                    2 -> {
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
        val delete =
            MenuObject("Delete").apply { setResourceValue(R.drawable.delete) }
        delete.setBgColorValue((Color.rgb(238, 238, 238)))
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