package com.example.cameragalery

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    var lastPath: String? = null

    var photos = arrayListOf<Photo>()

    var photosAdapter: PhotosAdapter? = null

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photosAdapter = PhotosAdapter(this, photos)
        adapterView.adapter = photosAdapter

        //loadImagesAsync.execute()
        loadImages()

        takePhoto.setOnClickListener() {
            dispatchTakePictureIntent()
        }

        //adapterView.adapter = ArrayAdapter<Bitmap>

    }



    val REQUEST_IMAGE_CAPTURE = 1

    val REQUEST_TAKE_PHOTO = 1


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.e("IOException", "Error occurred while creating the File")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.androidx.core.content.FileProvider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                //val imageBitmap = data?.extras?.get("data") as Bitmap
                //imageView.setImageBitmap(imageBitmap)
                val imgFile = File(currentPhotoPath)
                if (imgFile.exists()) {
                    val lastPhoto = BitmapFactory.decodeFile(imgFile.absolutePath)
                    photos.let { it.add(Photo(lastPhoto, currentPhotoPath)) }
                    photosAdapter!!.notifyDataSetChanged()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "CANCELED ", Toast.LENGTH_LONG).show()
            }
        }

    }

    //tentativa
    var loadImagesAsync = object : AsyncTask<Void, Void, Any?>(){
        override fun doInBackground(vararg params: Void?) : Any? {

            var directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            val files: Array<File> = directory!!.listFiles()!!
            Log.d("Files", "Size: " + files.size)
            for (i in files.indices) {
                var path = files[i].absolutePath
                path?.let{
                    var pth = it

                    BitmapFactory.decodeFile(path)?.let {
                        var bm = it

                        photos.add(Photo(bm ,pth))
                        photosAdapter!!.notifyDataSetChanged()
                        Log.d("Files", "File: " + i +" Name:" + files[i].name)


                    }
                }
            }
            Log.e("Files", "files loaded")

            return null
        }


    }

    fun loadImages() {

        var directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)



        val files: Array<File> = directory!!.listFiles()!!
        Log.d("Files", "Size: " + files.size)
        for (i in files.indices) {
            var path = files[i].absolutePath
            path?.let{
                var pth = it

                BitmapFactory.decodeFile(path)?.let {
                    var bm = it

                    photos.add(Photo(bm ,pth))
                    photosAdapter!!.notifyDataSetChanged()
                    Log.d("Files", "File: " + i +" Name:" + files[i].name)

                }
            }
        }
        Log.e("Files", "files loaded")

    }

}
