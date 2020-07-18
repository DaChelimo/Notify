package com.ramadan.notify.ui.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.ui.activity.PlayRecord
import com.ramadan.notify.ui.activity.Records
import com.ramadan.notify.utils.getRecordLength
import kotlinx.android.synthetic.main.record_item.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class RecordAdapter(private val activity: Records, private val filepath: Array<String?>) :
    RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {
    @SuppressLint("SimpleDateFormat")
    private val currentDate: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)
        return RecordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (filepath.isNotEmpty()) {
            filepath.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val file = File(filepath[position]!!)
        holder.customView(file)

    }

    inner class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        OnClickListener {
        private val mContext: Context = itemView.context
        fun customView(file: File) {
            val s = Date(file.lastModified())
            itemView.recordTitle.text = file.nameWithoutExtension
            itemView.recordLength.text =
                mContext.getRecordLength(getDuration(file)!!.toLong())
            itemView.recordDate.text = currentDate.format(s)

            itemView.setOnClickListener {
                try {
                    val playRecord = PlayRecord().newInstance(file)
                    val transaction: FragmentTransaction = (mContext as FragmentActivity)
                        .supportFragmentManager
                        .beginTransaction()
                    playRecord?.show(transaction, "dialog_playback")

                } catch (e: Exception) {
                    println(e)
                }
            }

            itemView.setOnLongClickListener {

                val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(mContext)
                val view: View = LayoutInflater.from(mContext).inflate(R.layout.option_dialog, null)
                val alertDialog = dialogBuilder.create()
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialogBuilder.setView(view)
                dialogBuilder.show()
                val share = view.findViewById<TextView>(R.id.share)
                val rename = view.findViewById<TextView>(R.id.rename)
                val delete = view.findViewById<TextView>(R.id.delete)
                share.setOnClickListener { }
                rename.setOnClickListener { }
                delete.setOnClickListener {
                    file.delete()
                    Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show()
                }
                false
            }

        }

        override fun onClick(view: View?) {
        }


        private fun getDuration(file: File): String? {
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(mContext, Uri.fromFile(file))
            val durationStr =
                mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            println(durationStr)
            return durationStr
        }

    }


}