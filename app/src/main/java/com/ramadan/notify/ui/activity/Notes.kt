package com.ramadan.notify.ui.activity

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ramadan.notify.R
import com.ramadan.notify.ui.adapter.NoteAdapter
import com.ramadan.notify.ui.viewModel.HomeViewModel
import com.ramadan.notify.ui.viewModel.HomeViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class Notes : Fragment(), KodeinAware {
    private lateinit var loadingDialog: AlertDialog
    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()
    private val viewModel by lazy {
        ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getNotes().observe(this, Observer {
            adapter.setDataList(it)
        })
        loadingDialog.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recycle_view, container, false)
        loadingDialog()
        loadingDialog.show()
        adapter = NoteAdapter(this)
        val recyclerView: RecyclerView = view.findViewById(R.id.dashboardRecycleView)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = adapter
        return view
    }

    private fun loadingDialog() {
        val dialogBuilder = AlertDialog.Builder(context)
        val layoutView = layoutInflater.inflate(R.layout.loading_dialog, null)
        dialogBuilder.setView(layoutView)
        loadingDialog = dialogBuilder.create()
        loadingDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}
