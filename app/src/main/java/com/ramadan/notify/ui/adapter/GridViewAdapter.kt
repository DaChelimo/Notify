package com.ramadan.notify.ui.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.ramadan.notify.R


class GridViewAdapter(private val activity: Activity, private val filepath: Array<String?>?) :
    BaseAdapter() {
    var bmp: Bitmap? = null
    override fun getCount(): Int {
        return filepath!!.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var vi: View? = convertView
        if (convertView == null) vi =
            inflater?.inflate(R.layout.whiteboard_item, null)
        val image: ImageView = vi?.findViewById(R.id.image) as ImageView
        val targetWidth = 100
        val targetHeight = 100
        val bmpOptions = BitmapFactory.Options()
        bmpOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filepath!![position], bmpOptions)
        val currHeight = bmpOptions.outHeight
        val currWidth = bmpOptions.outWidth
        var sampleSize = 1
        if (currHeight > targetHeight || currWidth > targetWidth) {
            sampleSize = if (currWidth > currHeight) Math.round(
                currHeight.toFloat()
                        / targetHeight.toFloat()
            ) else Math.round(
                currWidth.toFloat()
                        / targetWidth.toFloat()
            )
        }
        bmpOptions.inSampleSize = sampleSize
        bmpOptions.inJustDecodeBounds = false
        bmp = BitmapFactory.decodeFile(filepath[position], bmpOptions)
        println(bmp)
        println(filepath[position])
        image.setImageBitmap(bmp)
        image.setScaleType(ImageView.ScaleType.FIT_XY)
        bmp = null
        return vi
    }

    companion object {
        private var inflater: LayoutInflater? = null
    }

    init {
        inflater = activity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}