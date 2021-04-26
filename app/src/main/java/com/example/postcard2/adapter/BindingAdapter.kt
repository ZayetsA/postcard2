package com.example.postcard2.adapter

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.postcard2.BuildConfig
import com.example.postcard2.R
import com.google.android.material.textfield.TextInputLayout
import de.hdodenhof.circleimageview.CircleImageView

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
    if (isInteger(imageName)) {
        setBackgroundResource(imageName.toInt())
    } else {
        background = (
                Drawable.createFromStream(
                    resources.assets.open("${BuildConfig.BG_STRING}/$imageName"),
                    null
                )
                )
    }
}

@BindingAdapter("setProfileImage")
fun CircleImageView.setProfileImage(imageName: String) {
    if (imageName == "") {
        setImageResource(R.drawable.default_profile_image)
    } else {
        val imageBytes = Base64.decode(imageName, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        setImageBitmap(image)

    }
}

fun isInteger(s: String): Boolean {
    try {
        s.toInt()
    } catch (e: NumberFormatException) {
        return false
    } catch (e: NullPointerException) {
        return false
    }
    return true
}
