package com.example.cameragalery

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

class PhotosAdapter(context: Context, objects: MutableList<Photo>) :
    ArrayAdapter<Photo>(context, R.layout.list_item, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var photoView = convertView
        if (photoView == null) photoView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        photoView!!

        var imageView = photoView.findViewById<ImageView>(R.id.imageView)
        var currentPhoto: Photo = getItem(position)!!
        imageView.setImageBitmap(currentPhoto.thumbnail)

        var openImageIntent = Intent(context, photo_detail::class.java)
        openImageIntent.putExtra("path", currentPhoto.path)
        photoView.setOnClickListener() {
            context.startActivity(openImageIntent)
        }

        return photoView
    }

}