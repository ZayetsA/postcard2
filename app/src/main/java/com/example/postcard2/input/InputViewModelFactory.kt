package com.example.postcard2.input

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postcard2.sources.imagesource.implementation.AssetsImageSource

class InputViewModelFactory(
    private val imageSource: AssetsImageSource,
    private val assetsImageSource: AssetsImageSource,
    private val activity: FragmentActivity?
):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(InputViewModel::class.java)) {
            return InputViewModel(imageSource, assetsImageSource, activity) as T
        }
        throw IllegalArgumentException("Unknown member class")
    }

}