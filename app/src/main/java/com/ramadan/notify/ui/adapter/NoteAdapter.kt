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
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.data.repository.NoteRepository
import com.ramadan.notify.data.repository.Repository
import com.ramadan.notify.databinding.NoteItemBinding
import com.ramadan.notify.ui.activity.Note
import com.ramadan.notify.ui.activity.Notes
import com.ramadan.notify.utils.startHomeActivity
import com.ramadan.notify.utils.startNoteActivity


class NoteAdapter(val context: Notes) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataList = mutableListOf<WrittenNote>()
    private val viewNote = 0
    private val addNote = 1

    fun setDataList(data: MutableList<WrittenNote>) {
        dataList = data
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) addNote else viewNote
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == viewNote) {
            val binding: NoteItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.note_item, parent, false
            )
            ViewNoteViewHolder(binding)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.add_item, parent, false)
            AddNoteViewHolder(view)

        }
    }


    override fun getItemCount(): Int {
        return if (dataList.size > 0) {
            dataList.size + 1
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == viewNote) {
            val writtenNote: WrittenNote = dataList[position - 1]
            (holder as ViewNoteViewHolder).bind(writtenNote)
        } else {
            (holder as AddNoteViewHolder).addNote!!.setOnClickListener {
                holder.mContext.startActivity(Intent(holder.mContext, Note::class.java))
            }
        }
    }

    class ViewNoteViewHolder(private var binding: NoteItemBinding) :
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

    class AddNoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mContext: Context = itemView.context
        val addNote: ImageButton? = itemView.findViewById(R.id.addItem)
    }
}