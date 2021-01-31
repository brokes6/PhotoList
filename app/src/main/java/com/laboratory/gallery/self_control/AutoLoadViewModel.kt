package com.laboratory.gallery.self_control

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.toLiveData

class AutoLoadViewModel(application: Application) : AndroidViewModel(application){
    var pagedPhotoLiveData = PixabayDataFactory(application).toLiveData(1)

}