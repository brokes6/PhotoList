package com.laboratory.gallery.self_control

import android.content.Context
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.laboratory.gallery.PhotoDetails
import com.laboratory.gallery.Pixabay
import com.laboratory.gallery.VolleySingleton

class PixabayDataSource(private val context: Context) : PageKeyedDataSource<Int, PhotoDetails>() {
    private val queueKey =
        arrayOf("cat", "dog", "car", "beauty", "phone", "computer", "flower", "animal").random()

    override fun loadInitial(
        //这里是第一次加载
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoDetails>
    ) {
        val uri =
            "https://pixabay.com/api/?key=19761125-135f5272e8b1eda5e282dd499&q=${queueKey}&per_page=50&page=1"
        StringRequest(
            Request.Method.GET,
            uri,
            {
                callback.onResult(Gson().fromJson(it, Pixabay::class.java).hits.toList(), null, 2)
            },
            {

            }
        ).also {
            VolleySingleton.getInstance(context).requestQueue.add(it)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoDetails>) {
        val uri =
            "https://pixabay.com/api/?key=19761125-135f5272e8b1eda5e282dd499&q=${queueKey}&per_page=50&page=${params.key}}"
        StringRequest(
            Request.Method.GET,
            uri,
            {
                callback.onResult(
                    Gson().fromJson(it, Pixabay::class.java).hits.toList(), params.key + 1
                )
            },
            {

            }
        ).also { VolleySingleton.getInstance(context).requestQueue.add(it) }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoDetails>) {
        TODO("Not yet implemented")
    }
}