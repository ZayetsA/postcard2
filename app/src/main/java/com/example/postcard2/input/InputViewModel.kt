package com.example.postcard2.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.postcard2.SingleLiveEvent

class InputViewModel : ViewModel() {
    private val _navigationLiveEvent = SingleLiveEvent<NavDirections>()
    val navigationLiveEvent: LiveData<NavDirections> = _navigationLiveEvent
    val model = InputModel()

    private val _errorName = MutableLiveData<Boolean>()
    val errorName: LiveData<Boolean> = _errorName

    private val _errorTitle = MutableLiveData<Boolean>()
    val errorTitle: LiveData<Boolean> = _errorTitle

    private val _errorText = MutableLiveData<Boolean>()
    val errorText: LiveData<Boolean> = _errorText

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
}