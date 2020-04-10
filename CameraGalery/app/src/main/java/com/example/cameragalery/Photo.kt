package com.example.cameragalery

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Photo(var bitmap: Bitmap, var path: String) {

    val THUMBNAIL_SIZE = 160
    var thumbnail: Bitmap? =  Bitmap.createScaledBitmap(bitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false)

    constructor(path: String) : this(BitmapFactory.decodeFile(path), path)

}
