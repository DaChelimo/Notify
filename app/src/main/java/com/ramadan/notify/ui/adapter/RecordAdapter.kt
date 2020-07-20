package com.ramadan.notify.ui.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
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
                share.setOnClickListener {
                    shareFileDialog(file)
                    alertDialog.dismiss()
                }
                rename.setOnClickListener {
                    renameFileDialog(file)
                    alertDialog.dismiss()
                }
                delete.setOnClickListener {
                    file.delete()
                    Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
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

        private fun shareFileDialog(file: File) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(
                Intent.EXTRA_STREAM,
                Uri.fromFile(file)
            )
            shareIntent.type = "audio/mp3"
            mContext.startActivity(
                Intent.createChooser(
                    shareIntent,
                    mContext.getText(R.string.send_to)
                )
            )
        }

        fun renameFileDialog(file: File) {
            val renameFileBuilder = AlertDialog.Builder(mContext)
            val inflater = LayoutInflater.from(mContext)
            val view: View = inflater.inflate(R.layout.rename_dialog, null)
            val newName = view.findViewById<View>(R.id.new_name) as EditText
            val dirPath = Environment.getExternalStorageDirectory().path + "/Notify/Records/"

            renameFileBuilder.setTitle(mContext.getString(R.string.dialog_title_rename))
            renameFileBuilder.setCancelable(true)
            renameFileBuilder.setPositiveButton(mContext.getString(R.string.dialog_action_ok)) { dialog, id ->
                try {
                    val value = newName.text.toString() + ".mp3"
                    file.renameTo(File(dirPath + value))
                } catch (e: java.lang.Exception) {
                    Log.e("exception", e.message!!)
                }
                dialog.cancel()
            }
            renameFileBuilder.setNegativeButton(
                mContext.getString(R.string.dialog_action_cancel)
            ) { dialog, id -> dialog.cancel() }
            renameFileBuilder.setView(view)
            val alert = renameFileBuilder.create()
            alert.show()
        }
//
//        fun rename(position: Int, name: String) {
//            var mFilePath: String = Environment.getExternalStorageDirectory().absolutePath
//            mFilePath += "/SoundRecorder/$name"
//            val f = File(mFilePath)
//            if (f.exists() && !f.isDirectory) {
//                //file name is not unique, cannot rename file.
//                Toast.makeText(
//                    mContext,
//                    String.format(mContext.getString(R.string.toast_file_exists), name),
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                //file name is unique, rename file
//                val oldFilePath = File(getItem(position).getFilePath())
//                oldFilePath.renameTo(f)
//                mDatabase.renameItem(getItem(position), name, mFilePath)
//                notifyItemChanged(position)
//            }
//        }

    }


}