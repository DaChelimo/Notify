package com.ramadan.notify.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.data.model.Intro

class IntroAdapter(
    private val introList: List<Intro>
) : RecyclerView.Adapter<IntroAdapter.IntroAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroAdapterViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_intro, parent, false)
        return IntroAdapterViewHolder(root)
    }

    override fun getItemCount() = introList.size

    override fun onBindViewHolder(holder: IntroAdapterViewHolder, position: Int) {
        holder.bind(introList[position])
    }

    inner class IntroAdapterViewHolder(
        private val root: View
    ) : RecyclerView.ViewHolder(root) {
        private val title: TextView = root.findViewById(R.id.title)
        private val description: TextView = root.findViewById(R.id.description)
        private val button: TextView = root.findViewById(R.id.button)

        fun bind(intro: Intro) {
            title.setText(intro.title)
            description.setText(intro.description)
//            button.setOnClickListener {
//                val intent = Intent(root.context, intro.activityClass)
//                root.context.startActivity(intent)
//            }
        }
    }
}