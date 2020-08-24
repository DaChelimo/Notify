@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ramadan.notify.R
import com.ramadan.notify.ui.adapter.WhiteboardAdapter
import com.ramadan.notify.ui.viewModel.WhiteboardViewModel

class Whiteboards : Fragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(WhiteboardViewModel::class.java)
    }
    private lateinit var adapter: WhiteboardAdapter

    override fun onResume() {
        super.onResume()
        observeData()
    }

    fun observeData() {
        adapter = WhiteboardAdapter(this, viewModel.loadWhiteboards()!!)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recycle_view, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.dashboardRecycleView)
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        observeData()
        recyclerView.adapter = adapter
        return view
    }

}