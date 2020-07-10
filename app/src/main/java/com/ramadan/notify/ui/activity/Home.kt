@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.ramadan.notify.R
import com.ramadan.notify.ui.adapter.NoteAdapter
import com.ramadan.notify.ui.viewModel.HomeViewModel
import com.ramadan.notify.ui.viewModel.HomeViewModelFactory
import com.ramadan.notify.utils.colorAnimation
import com.ramadan.notify.utils.startRecordActivity
import com.ramadan.notify.utils.startTestActivity
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import com.yalantis.contextmenu.lib.MenuObject
import com.yalantis.contextmenu.lib.MenuParams
import kotlinx.android.synthetic.main.home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class Home : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()
    private val viewModel by lazy {
        ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }
    private lateinit var adapter: NoteAdapter
    private lateinit var contextMenuDialogFragment: ContextMenuDialogFragment
    private val container by lazy { findViewById<View>(R.id.container) }

    //    private val title by lazy { findViewById<TextView>(R.id.title) }
    private val bottomBar by lazy { findViewById<ChipNavigationBar>(R.id.bottom_bar) }
    private var lastColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        adapter = NoteAdapter(this)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
//        lastColor = (container.background as ColorDrawable).color
        bottomBar.setOnItemSelectedListener { id ->
            when (id) {
//                R.id.notes -> "notes"
                R.id.whiteboard -> startTestActivity()
                R.id.records -> startRecordActivity()
//                else -> "rrr"
            }
            val option = when (id) {
                R.id.notes -> R.color.colorPrimaryLight to "Notes"
                R.id.whiteboard -> R.color.colorPrimary to "Whiteboard"
                R.id.records -> R.color.colorPrimaryLight to "Records"
                else -> R.color.white to ""
            }
            val color = ContextCompat.getColor(this, option.first)
            container.colorAnimation(lastColor, color)
            lastColor = color

//            title.text = option.second
        }


        initMenuFragment()
        observeDate()
    }

    override fun onStart() {
        super.onStart()
        bottomBar.setItemSelected(R.id.notes)
    }

    private fun observeDate() {
        viewModel.getNotes().observe(this, Observer {
            adapter.setDataList(it)
        })
        progressBar.visibility = View.GONE

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
                        Intent(view.context, NewNote::class.java).also {
                            startActivity(it)
                        }
                    }
                    2 -> {
                        Intent(view.context, Whiteboard::class.java).also {
                            startActivity(it)
                        }
                    }
                    3 -> {
                        viewModel.logout(view)
                    }
                }
            }
        }
    }

    private fun getMenuObjects() = mutableListOf<MenuObject>().apply {
        val empty = MenuObject().apply {}
        empty.setBgColorValue((Color.BLACK))
        val newNote =
            MenuObject("New note").apply { setResourceValue(R.drawable.new_note) }
        newNote.setBgColorValue((Color.rgb(32, 32, 32)))
        val whiteBoard =
            MenuObject("Whiteboard").apply { setResourceValue(R.drawable.new_whiteboard) }
        whiteBoard.setBgColorValue((Color.BLACK))
        val logOut =
            MenuObject("Logout").apply { setResourceValue(R.drawable.logout) }
        logOut.setBgColorValue((Color.rgb(32, 32, 32)))
        add(empty)
        add(newNote)
        add(whiteBoard)
        add(logOut)
    }

    private fun showContextMenuDialogFragment() {
        if (supportFragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
            contextMenuDialogFragment.show(supportFragmentManager, ContextMenuDialogFragment.TAG)
        }
    }

}
