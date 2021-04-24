package com.example.postcard2.adapter

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorMessage")
fun TextInputLayout.errorMessage(errorState: Boolean) {
    error = if (errorState) {
        "Field can not be empty!"
    } else ""
}