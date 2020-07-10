@file:Suppress("DEPRECATION")

package com.ramadan.notify.ui.activity

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ramadan.notify.R
import com.ramadan.notify.ui.adapter.WhiteboardAdapter
import kotlinx.android.synthetic.main.activity_gallery_picture.*
import java.io.File


class Test : Activity() {
    private var FilePathStrings: Array<String?>? = null
    private var listFile: Array<File>? = null
    var adapter: WhiteboardAdapter? = null
    var file: File? = null
    var imageview: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_picture)
        // Check for SD Card
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED
        ) {
            Toast.makeText(
                this, "Error! No SDCARD Found!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            file = File(
                Environment.getExternalStorageDirectory()
                    .path + "/Notify/Whiteboard"
            )
        }
        if (file!!.isDirectory) {
            listFile = file!!.listFiles()
            FilePathStrings = arrayOfNulls(listFile!!.size)
            for (i in listFile!!.indices) {
                FilePathStrings!![i] = listFile!![i].absolutePath
            }
        }
        println(FilePathStrings?.size)
        adapter = WhiteboardAdapter(this, FilePathStrings!!)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = adapter


//        recyclerView!!.onItemClickListener =
//            OnItemClickListener { parent, view, position, id ->
//                imageview = findViewById<ImageView>(R.id.imageView1)
//                val targetWidth = 700
//                val targetHeight = 500
//                val bmpOptions = BitmapFactory.Options()
//                bmpOptions.inJustDecodeBounds = true
//                BitmapFactory.decodeFile(
//                    FilePathStrings!![position],
//                    bmpOptions
//                )
//                val currHeight = bmpOptions.outHeight
//                val currWidth = bmpOptions.outWidth
//                var sampleSize = 1
//                if (currHeight > targetHeight || currWidth > targetWidth) {
//                    sampleSize = if (currWidth > currHeight) Math.round(
//                        currHeight.toFloat()
//                                / targetHeight.toFloat()
//                    ) else Math.round(
//                        currWidth.toFloat()
//                                / targetWidth.toFloat()
//                    )
//                }
//                bmpOptions.inSampleSize = sampleSize
//                bmpOptions.inJustDecodeBounds = false
//                bmp = BitmapFactory.decodeFile(
//                    FilePathStrings!![position],
//                    bmpOptions
//                )
//                imageview?.setImageBitmap(bmp)
//                imageview?.scaleType = ImageView.ScaleType.FIT_XY
//                bmp = null
//            }
    }
} //class
