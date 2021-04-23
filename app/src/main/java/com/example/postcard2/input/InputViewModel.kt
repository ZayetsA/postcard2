package com.example.postcard2.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.postcard2.SingleLiveEvent

class InputViewModel : ViewModel() {
    private val _navigationLiveEvent = SingleLiveEvent<NavDirections>()
    val navigationLiveEvent: LiveData<NavDirections> = _navigationLiveEvent

    fun showCardFragment() {
        _navigationLiveEvent.value =
            InputFragmentDirections.actionInputFragmentToCardFragment()
    }
}