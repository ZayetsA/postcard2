package com.example.postcard2.sources.imagesource.implementation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.postcard2.sources.imagesource.ImageSource
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class AssetsImageSource @Throws(FileNotFoundException::class) constructor(
    private val context: Context,
    private val folderPath: String
) : ImageSource {

    private val listOfPaths: ArrayList<String> =
        context.assets.list(folderPath)?.toList() as ArrayList<String>

    override fun getBitmap(name: String): Bitmap {
        val resource =
            try {
                context.assets.open(folderPath.plus(File.separator).plus(name))
            } catch (exc: IOException) {
                throw FileNotFoundException("No such file at $name")
            }

        return BitmapFactory.decodeStream(resource)
            ?: throw Exception("Can't convert file $name to bitmap")
    }

    override fun list(): ArrayList<String> {
        return listOfPaths
    }
}