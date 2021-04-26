package com.example.postcard2.input

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import kotlinx.android.parcel.Parcelize

@Parcelize
class InputModel(
    private var _name: String = "",
    private var _title: String = "",
    private var _text: String = "",
    private var _imageName: String = "",
    private var _backgroundImage: String = "",
    private var _profileImage: String = ""
) : BaseObservable(), Parcelable {

    @get:Bindable
    var name: String = _name
        set(value) {
            _name = value
            field = value
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var title: String = _title
        set(value) {
            _title = value
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    var text: String = _text
        set(value) {
            _text = value
            field = value
            notifyPropertyChanged(BR.text)
        }


    @get:Bindable
    var imageName: String = _imageName
        set(value) {
            _imageName = value
            field = value
            notifyPropertyChanged(BR.imageName)
        }

    @get:Bindable
    var backgroundImage: String = _backgroundImage
        set(value) {
            _backgroundImage = value
            field = value
            notifyPropertyChanged(BR.backgroundImage)
        }

    @get:Bindable
    var profileImage: String = _profileImage
        set(value) {
            _profileImage = value
            field = value
            notifyPropertyChanged(BR.backgroundImage)
        }

    fun isError() = _name.isEmpty() || _title.isEmpty() || _text.isEmpty()
}