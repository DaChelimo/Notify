@file:Suppress("DEPRECATION")

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.ui.activity.Whiteboards
import com.ramadan.notify.utils.startAppIntroActivity
import com.ramadan.notify.utils.startHomeActivity
import kotlinx.android.synthetic.main.whiteboard_item.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class WhiteboardAdapter(private val filepath: Array<String?>) :
    RecyclerView.Adapter<WhiteboardAdapter.WhiteboardViewHolder>() {
    private var bitmap: Bitmap? = null
    val dirPath = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    ).path + "/Notify/"

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
        if (bitmap != null) {
            holder.customView(bitmap!!)
            holder.itemView.whiteboardTitle.text = file.nameWithoutExtension
            holder.itemView.whiteboardDate.text = currentDate.format(date)
            holder.itemView.setOnLongClickListener {
                holder.showOption(file)
                false
            }
        } else {
            holder.itemView.context.startHomeActivity()
        }
    }


    inner class WhiteboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext: Context = itemView.context
        private val whiteboards = Whiteboards()

        fun customView(bitmap: Bitmap) {
            itemView.whiteboardImg.setImageBitmap(bitmap)
            itemView.whiteboardImg.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.zoom_in
            )
            itemView.setOnClickListener {
//                whiteboards.showWhiteboard(bitmap, mContext)
                mContext.startAppIntroActivity()
            }
        }


        fun showOption(file: File) {
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(mContext)
            val view = View.inflate(mContext, R.layout.option_dialog, null)
            dialogBuilder.setView(view)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            alertDialog.setCancelable(true)
            val share = view.findViewById<TextView>(R.id.share)
            val rename = view.findViewById<TextView>(R.id.rename)
            val delete = view.findViewById<TextView>(R.id.delete)
            share.setOnClickListener {
                shareWhiteboard(file)
                alertDialog.dismiss()
            }
            rename.setOnClickListener {
                renameWhiteboard(file)
                alertDialog.dismiss()
            }
            delete.setOnClickListener {
                file.delete()
                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show()
                alertDialog.cancel()
                mContext.startHomeActivity()
            }
        }

        private fun shareWhiteboard(file: File) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            shareIntent.type = "picture/jpg"
            mContext.startActivity(Intent.createChooser(shareIntent, "Send to"))
        }

        private fun renameWhiteboard(file: File) {
            val dialogBuilder = AlertDialog.Builder(mContext)
            val view = View.inflate(mContext, R.layout.rename_dialog, null)
            dialogBuilder.setView(view)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            val title = view.findViewById<TextView>(R.id.title)
            title.text = "Whiteboard name"
            val newName = view.findViewById<View>(R.id.new_name) as EditText
            val confirm = view.findViewById<TextView>(R.id.confirm)
            val cancel = view.findViewById<TextView>(R.id.cancel)
            confirm.setOnClickListener {
                try {
                    val value = newName.text.toString() + ".jpg"
                    file.renameTo(File(dirPath + value))
                    Toast.makeText(mContext, "Renamed", Toast.LENGTH_SHORT).show()
                    alertDialog.cancel()
                    mContext.startHomeActivity()
                } catch (e: java.lang.Exception) {
                    mContext.startHomeActivity()
                }
            }
            cancel.setOnClickListener { alertDialog.cancel() }
        }
    }
}