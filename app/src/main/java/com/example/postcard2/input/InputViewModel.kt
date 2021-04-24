package com.example.postcard2.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.postcard2.SingleLiveEvent
import com.example.postcard2.commons.Uroboros
import com.example.postcard2.sources.imagesource.implementation.AssetsImageSource

class InputViewModel(imageSource: AssetsImageSource, assetsImageSource: AssetsImageSource) :
    ViewModel() {
    private val _navigationLiveEvent = SingleLiveEvent<NavDirections>()
    val navigationLiveEvent: LiveData<NavDirections> = _navigationLiveEvent
    val model = InputModel()

    private val iterator = Uroboros(imageSource.list(), imageSource.list().first())
    private val bgIterator = Uroboros(assetsImageSource.list(), assetsImageSource.list().first())

    private val _errorName = MutableLiveData<Boolean>()
    val errorName: LiveData<Boolean> = _errorName

    private val _errorTitle = MutableLiveData<Boolean>()
    val errorTitle: LiveData<Boolean> = _errorTitle

    private val _errorText = MutableLiveData<Boolean>()
    val errorText: LiveData<Boolean> = _errorText

    init {
        model.imageName = iterator.get()
        model.backgroundImage = bgIterator.get()
    }

    fun showCardFragment() {
        model.apply {
            _errorName.value = name.isEmpty()
            _errorTitle.value = title.isEmpty()
            _errorText.value = text.isEmpty()
            if (!isError()) {
                _navigationLiveEvent.value =
                    InputFragmentDirections.actionInputFragmentToCardFragment(model)
            }
        }
    }

    fun getNextBackground() {
        model.imageName = iterator.next().get()
    }

    fun getPreviousBackground() {
        model.imageName = iterator.previous().get()
    }

    fun nextBackground() {
        model.backgroundImage = bgIterator.next().get()
    }
}