package com.laboratory.gallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class GalleryViewModel(application: Application) : AndroidViewModel(application) {
    private val _photoListLive = MutableLiveData<List<PhotoDetails>>()
    val photoListLive: LiveData<List<PhotoDetails>>
        get() = _photoListLive
    private val keyWord =
        arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flower", "animal")
    private var currentPage = 1
    private var isNewQuery = true
    private lateinit var currentKey: String

    fun resetQuery() {
        currentPage = 1
        currentKey = keyWord.random()
        isNewQuery = true
        fetData()
    }

    fun fetData() {
        val stringRequest = StringRequest(
            Request.Method.GET,
            getUrl(),
            {
                with(Gson().fromJson(it, Pixabay::class.java)) {
                    if (isNewQuery) {
                        _photoListLive.value = hits.toList()
                    } else {
                        _photoListLive.value =
                            arrayListOf(_photoListLive.value!!, hits.toList()).flatten()
                    }
                    isNewQuery = false
                    currentPage++
                }
            },
            {
                isNewQuery = false
                Log.e("TAG", "fetData: 获取图片出错+$it")
            }
        )
        VolleySingleton.getInstance(getApplication()).requestQueue.add(stringRequest)
    }

    private fun getUrl(): String {
        return "https://pixabay.com/api/?key=19761125-135f5272e8b1eda5e282dd499&q=${currentKey}&page=${currentPage}"
    }
}