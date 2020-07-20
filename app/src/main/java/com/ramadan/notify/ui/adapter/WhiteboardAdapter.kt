package com.ramadan.notify.ui.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.ui.activity.Whiteboard
import com.ramadan.notify.ui.activity.Whiteboards
import kotlinx.android.synthetic.main.whiteboard_item.view.*


class WhiteboardAdapter(private val activity: Whiteboards, private val filepath: Array<String?>) :
    RecyclerView.Adapter<WhiteboardAdapter.WhiteboardViewHolder>() {
    private var bitmap: Bitmap? = null

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
        val bmpOptions = BitmapFactory.Options()
        bmpOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filepath[position], bmpOptions)
        bmpOptions.inSampleSize = 2
        bmpOptions.inJustDecodeBounds = false
        bitmap = BitmapFactory.decodeFile(filepath[position], bmpOptions)
        holder.customView(bitmap!!)
    }


    inner class WhiteboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun customView(bitmap: Bitmap) {
            itemView.whiteboardImg.setImageBitmap(bitmap)
//            itemView.whiteboardTitle.text =
//                filepath[adapterPosition].
                itemView.setOnClickListener(View.OnClickListener {

                    Intent(itemView.context, Whiteboard::class.java).also {
                        it.putExtra("bitmap", filepath)
                        println(bitmap.toString())
//                    it.putExtra("bitmap1", bitmap)
                        itemView.context.startActivity(it)
                    }

                })
        }
    }
}