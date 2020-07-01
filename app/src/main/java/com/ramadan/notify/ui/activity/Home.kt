package com.ramadan.notify.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ramadan.notify.R
import com.ramadan.notify.databinding.HomeBinding
import com.ramadan.notify.ui.adapter.NoteAdapter
import com.ramadan.notify.ui.viewModel.HomeViewModel
import com.ramadan.notify.ui.viewModel.HomeViewModelFactory
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
    private lateinit var binding: HomeBinding
    private lateinit var contextMenuDialogFragment: ContextMenuDialogFragment
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home)
        adapter = NoteAdapter(this)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        initMenuFragment()
        observeDate()
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
                        Intent(view.context, DrawNote::class.java).also {
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
            MenuObject("Whiteboard").apply { setResourceValue(R.drawable.whiteboard) }
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
