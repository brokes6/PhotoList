package com.laboratory.gallery

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.gallery_cell.view.*


class GalleryCellAdapter(private val activity: Activity) :
    ListAdapter<PhotoDetails, GalleryViewHolder>(DIFFCALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val gallerView = GalleryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell, parent, false)
        )
        gallerView.itemView.ImageView.setOnClickListener {
            val intent  = Intent(activity,PhotoActivity::class.java).apply {
                putParcelableArrayListExtra("PHOTO_LIST", ArrayList(currentList))
                putExtra("PHOTO_POSITION", gallerView.adapterPosition)
            }
            activity.startActivity(intent)
        }
        return gallerView
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.itemView.shimmerLayoutGallery.apply {
            setShimmerColor(0X55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }
        holder.itemView.ImageView.layoutParams.height = getItem(position).thumbnailHeight
        Glide.with(holder.itemView).load(getItem(position).thumbnailImage)
            .placeholder(R.drawable.gray_radius_background)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also { holder.itemView.shimmerLayoutGallery.stopShimmerAnimation() }
                }
            }).into(holder.itemView.ImageView)
    }

    object DIFFCALLBACK : DiffUtil.ItemCallback<PhotoDetails>() {

        override fun areItemsTheSame(oldItem: PhotoDetails, newItem: PhotoDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoDetails, newItem: PhotoDetails): Boolean {
            return oldItem == newItem
        }
    }
}

class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)