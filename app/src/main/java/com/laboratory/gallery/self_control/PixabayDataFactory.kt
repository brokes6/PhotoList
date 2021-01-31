package com.laboratory.gallery.self_control

import android.content.Context
import com.laboratory.gallery.PhotoDetails
import javax.sql.DataSource

class PixabayDataFactory(private val context: Context) : androidx.paging.DataSource.Factory<Int,PhotoDetails>(){

    override fun create(): androidx.paging.DataSource<Int, PhotoDetails> {
        return PixabayDataSource(context)
    }
}