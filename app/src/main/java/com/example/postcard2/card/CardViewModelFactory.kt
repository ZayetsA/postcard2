package com.example.postcard2.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.postcard2.input.InputModel

class CardViewModelFactory(private val model: InputModel) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            return CardViewModel(model) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}