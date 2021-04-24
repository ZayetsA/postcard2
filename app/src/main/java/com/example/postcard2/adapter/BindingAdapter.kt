package com.example.postcard2.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.postcard2.BuildConfig
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorMessage")
fun TextInputLayout.errorMessage(errorState: Boolean) {
    error = if (errorState) {
        "Field can not be empty!"
    } else ""
}

@BindingAdapter("setImageFromAssets")
fun ImageView.setImageFromAssets(imageName: String) {
    setImageDrawable(
        Drawable.createFromStream(
            resources.assets.open("${BuildConfig.FOO_STRING}/$imageName"),
            null
        )
    )
}

@BindingAdapter("setBackgroundImage")
fun ConstraintLayout.setBackgroundImage(imageName: String) {
    background = (
        Drawable.createFromStream(
            resources.assets.open("${BuildConfig.BG_STRING}/$imageName"),
            null
        )
    )
}
