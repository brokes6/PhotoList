package com.laboratory.gallery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Pixabay(
    val total: Int,
    val totalHits: Int,
    val hits: Array<PhotoDetails>
)

@Parcelize
data class PhotoDetails(
    val id: Int,
    @SerializedName("webformatURL") val thumbnailImage: String,
    @SerializedName("webformatHeight") val thumbnailHeight: Int,
    val user: String,
    @SerializedName("largeImageURL") val masterMap: String,
    @SerializedName("imageHeight") val imageHeight: Int,
    @SerializedName("likes") val like: Int,
    @SerializedName("comments") val comment: Int,
    @SerializedName("favorites") val forward: Int,
    @SerializedName("downloads") val collection: Int,

    ) : Parcelable
