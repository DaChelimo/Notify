package com.ramadan.notify.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.data.model.WrittenNote
import com.ramadan.notify.databinding.NoteItemBinding
import com.ramadan.notify.ui.activity.Notes
import com.ramadan.notify.utils.startNoteActivity
import java.io.File


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
        RecyclerView.ViewHolder(binding.root), OnClickListener {
        private val mContext: Context = itemView.context
        fun bind(writtenNote: WrittenNote) {
            binding.noteItem = writtenNote
            binding.note.setCardBackgroundColor(writtenNote.noteColor)
            binding.executePendingBindings()
            itemView.setOnClickListener {
                it.context.startNoteActivity(writtenNote)
            }
        }

        override fun onClick(view: View?) {
        }

        private fun showOption(file: File) {
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(mContext)
            val view: View = View.inflate(mContext, R.layout.option_dialog, null)
            dialogBuilder.setView(view)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBuilder.show()
            alertDialog.setCancelable(true)
            val share = view.findViewById<TextView>(R.id.share)
            val rename = view.findViewById<TextView>(R.id.rename)
            val delete = view.findViewById<TextView>(R.id.delete)
            share.setOnClickListener {
                shareRecord(file)
                alertDialog.dismiss()
            }
            rename.setOnClickListener {
                renameRecord(file)
                alertDialog.dismiss()
            }
            delete.setOnClickListener {
                file.delete()
                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show()
                alertDialog.dismiss()
            }
        }


        private fun shareRecord(file: File) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            shareIntent.type = "audio/mp3"
            mContext.startActivity(Intent.createChooser(shareIntent, "Send to"))
        }

        private fun renameRecord(file: File) {
            val renameFileBuilder = AlertDialog.Builder(mContext)
            val inflater = LayoutInflater.from(mContext)
            val view: View = inflater.inflate(R.layout.rename_dialog, null)
            val newName = view.findViewById<View>(R.id.new_name) as EditText
            val dirPath = Environment.getExternalStorageDirectory().path + "/Notify/Records/"

            renameFileBuilder.setTitle("Rename")
            renameFileBuilder.setCancelable(true)
            renameFileBuilder.setPositiveButton("Ok") { dialog, id ->
                try {
                    val value = newName.text.toString() + ".mp3"
                    file.renameTo(File(dirPath + value))
                } catch (e: java.lang.Exception) {
                    Log.e("exception", e.message!!)
                }
                dialog.cancel()
            }
            renameFileBuilder.setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }
            renameFileBuilder.setView(view)
            val alert = renameFileBuilder.create()
            alert.show()
        }

    }
}