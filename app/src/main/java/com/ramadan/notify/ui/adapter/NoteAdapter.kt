package com.ramadan.notify.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.data.repository.NoteRepository
import com.ramadan.notify.data.repository.Repository
import com.ramadan.notify.databinding.NoteItemBinding
import com.ramadan.notify.ui.activity.Notes
import com.ramadan.notify.utils.startHomeActivity
import com.ramadan.notify.utils.startNoteActivity
import kotlinx.android.synthetic.main.note.view.*
import kotlinx.android.synthetic.main.note_item.view.*


class NoteAdapter(val context: Notes) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
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
        RecyclerView.ViewHolder(binding.root) {
        private val mContext: Context = itemView.context
        fun bind(writtenNote: WrittenNote) {
            binding.noteItem = writtenNote

            binding.note.setCardBackgroundColor(writtenNote.noteColor)
            binding.executePendingBindings()
            itemView.setOnClickListener {
                it.context.startNoteActivity(writtenNote)
            }

            itemView.setOnLongClickListener {
                showOption(writtenNote)
                false
            }
        }

        private fun showOption(writtenNote: WrittenNote) {
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(mContext)
            val view = View.inflate(mContext, R.layout.option_dialog, null)
            dialogBuilder.setView(view)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            alertDialog.setCancelable(true)
            val share = view.findViewById<TextView>(R.id.share)
            val rename = view.findViewById<TextView>(R.id.rename)
            rename.visibility = GONE
            val delete = view.findViewById<TextView>(R.id.delete)
            share.setOnClickListener {
                shareNote(writtenNote.content)
                alertDialog.cancel()
            }
            delete.setOnClickListener {
                NoteRepository(Repository()).deleteNote(writtenNote.ID)
                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show()
                alertDialog.cancel()
                mContext.startHomeActivity()
            }
        }


        private fun shareNote(noteContent: String) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, noteContent)
            shareIntent.type = "text/plain"
            mContext.startActivity(Intent.createChooser(shareIntent, "Send to"))
        }
    }
}