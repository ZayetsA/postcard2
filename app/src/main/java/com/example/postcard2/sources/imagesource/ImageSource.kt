package com.example.postcard2.sources.imagesource

import android.graphics.Bitmap

interface ImageSource {

    fun getBitmap(name: String): Bitmap

    fun list(): List<String>
}
