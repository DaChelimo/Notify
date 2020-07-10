package com.ramadan.notify.ui.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ramadan.notify.R
import com.ramadan.notify.data.model.Whiteboard
import kotlinx.android.synthetic.main.testo.view.*


class WhiteboardAdapter(private val activity: Activity, private val filepath: Array<String?>) :
    RecyclerView.Adapter<WhiteboardAdapter.WhiteboardViewHolder>() {
    private var bitmap: Bitmap? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhiteboardViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.testo, parent, false)

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
        val sampleSize = 1
        bmpOptions.inSampleSize = sampleSize
        bmpOptions.inJustDecodeBounds = false
        bitmap = BitmapFactory.decodeFile(filepath[position], bmpOptions)
        println(bitmap)
        println(filepath[position])
        holder.customView(bitmap!!)
    }


    inner class WhiteboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun customView(bitmap: Bitmap) {
            itemView.image.setImageBitmap(bitmap)
            itemView.image.scaleType = ImageView.ScaleType.FIT_XY

            itemView.setOnClickListener(View.OnClickListener {
                val intent = Intent(itemView.context, Whiteboard::class.java)

                itemView.context.startActivity(intent)
            })

        }
    }
}