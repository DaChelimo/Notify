package com.ramadan.notify.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.databinding.NoteItemBinding
import com.ramadan.notify.utils.startNewNoteActivity


class RecordAdapter(val context: Context) :
    RecyclerView.Adapter<RecordAdapter.NoteViewHolder>() {
    private var dataList = mutableListOf<WrittenNote>()

    fun setDataList(data: MutableList<WrittenNote>) {
        dataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding: NoteItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.note_item, parent, false
        )
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val writtenNote: WrittenNote = dataList[position]
        holder.bind(writtenNote)
    }

    inner class NoteViewHolder(private var binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        fun bind(writtenNote: WrittenNote) {
            binding.noteItem = writtenNote
            binding.executePendingBindings()
            itemView.setOnClickListener {
                it.context.startNewNoteActivity(writtenNote)
            }
        }

        override fun onClick(view: View?) {
        }
    }
}