package com.ramadan.notify.ui.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.ui.activity.Whiteboards
import kotlinx.android.synthetic.main.whiteboard_item.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class WhiteboardAdapter(private val activity: Whiteboards, private val filepath: Array<String?>) :
    RecyclerView.Adapter<WhiteboardAdapter.WhiteboardViewHolder>() {
    private var bitmap: Bitmap? = null
    private val mContext: Context = activity.context!!

    @SuppressLint("SimpleDateFormat")
    private val currentDate: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhiteboardViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.whiteboard_item, parent, false)
        return WhiteboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (filepath.isNotEmpty()) {
            filepath.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: WhiteboardViewHolder, position: Int) {
        val file = File(filepath[position]!!)
        val date = Date(file.lastModified())
        val bmpOptions = BitmapFactory.Options()
        bmpOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.path, bmpOptions)
        bmpOptions.inSampleSize = 2
        bmpOptions.inJustDecodeBounds = false
        bitmap = BitmapFactory.decodeFile(file.path, bmpOptions)
        holder.customView(bitmap!!)
        holder.itemView.whiteboardTitle.text = file.nameWithoutExtension
        holder.itemView.whiteboardDate.text = currentDate.format(date)
        holder.itemView.setOnLongClickListener {
            holder.showOption(file)
            false
        }


    }





    inner class WhiteboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext: Context = itemView.context
        fun customView(bitmap: Bitmap) {
            itemView.whiteboardImg.setImageBitmap(bitmap)
        }

        fun showOption(file: File) {
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
                Toast.makeText(mContext, "Renamed", Toast.LENGTH_SHORT).show()
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
            shareIntent.type = "picture/jpg"
            mContext.startActivity(Intent.createChooser(shareIntent, "Send to"))
        }

        private fun renameRecord(file: File) {
            val renameFileBuilder = AlertDialog.Builder(mContext)
            val inflater = LayoutInflater.from(mContext)
            val view: View = inflater.inflate(R.layout.rename_dialog, null)
            val newName = view.findViewById<View>(R.id.new_name) as EditText
            val dirPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ).path.toString() + "/Notify/"

            renameFileBuilder.setTitle("Rename")
            renameFileBuilder.setCancelable(true)
            renameFileBuilder.setPositiveButton("Ok") { dialog, id ->
                try {
                    val value = newName.text.toString() + ".jpg"
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