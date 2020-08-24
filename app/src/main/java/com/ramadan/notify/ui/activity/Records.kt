package com.ramadan.notify.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.ui.adapter.RecordAdapter
import com.ramadan.notify.ui.viewModel.RecordViewModel

class Records : Fragment() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RecordViewModel::class.java)
    }
    private lateinit var adapter: RecordAdapter

    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun observeData() {
        adapter = RecordAdapter(this, viewModel.loadRecords()!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recycle_view, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.dashboardRecycleView)
        recyclerView.layoutManager = GridLayoutManager(view.context,2)
        observeData()
        recyclerView.adapter = adapter
        return view
    }

}