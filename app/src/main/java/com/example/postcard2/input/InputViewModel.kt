package com.example.postcard2.input

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.postcard2.SingleLiveEvent
import com.example.postcard2.commons.Uroboros
import com.example.postcard2.events.Event
import com.example.postcard2.presets.PresetModel
import com.example.postcard2.sources.imagesource.implementation.AssetsImageSource
import com.google.gson.Gson
import java.util.*


class InputViewModel(
    imageSource: AssetsImageSource,
    assetsImageSource: AssetsImageSource,
    private val preferences: SharedPreferences?,
) :
    ViewModel() {
    var listOfThemes = ArrayList<PresetModel>()
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

    private val _shouldCloseLiveData = MutableLiveData<Event<Boolean>>()
    val shouldCloseLiveData: LiveData<Event<Boolean>> = _shouldCloseLiveData

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

    fun launchCard() {
        model.apply {
            _errorName.value = name.isEmpty()
            _errorTitle.value = title.isEmpty()
            _errorText.value = text.isEmpty()
            if (!isError()) {
                val editor = preferences?.edit()
                val gson = Gson()
                val json = gson.toJson(model)
                editor?.putString("Card", json)
                editor?.apply()
                closeAppAction()
            }
        }
    }

    fun checkArgs() {
        if (preferences?.getString("Card", "") != "") {
            val gson = Gson()
            val json: String? = preferences?.getString("Card", "")
            val model: InputModel = gson.fromJson(json, InputModel::class.java)
            _navigationLiveEvent.value =
                InputFragmentDirections.actionInputFragmentToCardFragment(model)
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

    private fun addTheme(title: String, text: String, drawable: Int) {
        listOfThemes.add(
            PresetModel(title, text, drawable)
        )
    }

    fun closeAppAction(){
        _shouldCloseLiveData.postValue(Event(true))
    }

    fun addDefaultThemes() {
        addTheme("Happy lines", "Nice background", com.example.postcard2.R.drawable.background_1)
        addTheme("Poehali!", "Space background", com.example.postcard2.R.drawable.background_2)
        addTheme(
            "Some space",
            "Another space background",
            com.example.postcard2.R.drawable.background_3
        )
        addTheme("Lava lamp", "Lava background", com.example.postcard2.R.drawable.background_4)
    }
}