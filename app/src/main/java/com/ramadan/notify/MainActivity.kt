package com.ramadan.notify

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ramadan.notify.ui.activity.*
import com.ramadan.notify.ui.adapter.ViewPagerAdapter
import com.ramadan.notify.ui.viewModel.HomeViewModel
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import com.yalantis.contextmenu.lib.MenuObject
import com.yalantis.contextmenu.lib.MenuParams


class MainActivity : AppCompatActivity() {
    private val whiteboards: Whiteboards = Whiteboards()
    private val notes: Notes = Notes()
    private val records: Records = Records()
    private lateinit var viewModel: HomeViewModel
    private lateinit var contextMenuDialogFragment: ContextMenuDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabLayout: TabLayout = findViewById(R.id.tabs)
        val viewPagerAdapter =
            ViewPagerAdapter(supportFragmentManager, 0)
        viewPagerAdapter.addFragment(notes)
        viewPagerAdapter.addFragment(whiteboards)
        viewPagerAdapter.addFragment(records)
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.note)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.whiteboard)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.record)
//        supportActionBar?.isHideOnContentScrollEnabled = true
        initMenuFragment()
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
                        Intent(view.context, Note::class.java).also {
                            startActivity(it)
                        }
                    }
                    1 -> {
                        Intent(view.context, Whiteboard::class.java).also {
                            startActivity(it)
                        }
                    }
                    2 -> {
                        Intent(view.context, Record::class.java).also {
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
        val note =
            MenuObject("New note").apply { setResourceValue(R.drawable.note) }
        note.setBgColorValue((Color.rgb(238, 238, 238)))
        val whiteboard =
            MenuObject("New Whiteboard").apply { setResourceValue(R.drawable.whiteboard) }
        whiteboard.setBgColorValue((Color.WHITE))
        val record =
            MenuObject("New record").apply { setResourceValue(R.drawable.record) }
        record.setBgColorValue((Color.rgb(238, 238, 238)))
        val logOut =
            MenuObject("Logout").apply { setResourceValue(R.drawable.logout) }
        logOut.setBgColorValue((Color.WHITE))
        add(note)
        add(whiteboard)
        add(record)
        add(logOut)
    }

    private fun showContextMenuDialogFragment() {
        if (supportFragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
            contextMenuDialogFragment.show(supportFragmentManager, ContextMenuDialogFragment.TAG)
        }
    }

}