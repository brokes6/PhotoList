package com.laboratory.gallery.self_control

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.laboratory.gallery.PhotoDetails
import com.laboratory.gallery.R
import kotlinx.android.synthetic.main.indicator_item.view.*

class photoIndicatorAdapter : ListAdapter<PhotoDetails, photoIndicatorViewHolder>(DIFFCALLBACK) {
    private var isSelected = 0
    private lateinit var listener: itemOnClickListener

    object DIFFCALLBACK : DiffUtil.ItemCallback<PhotoDetails>() {

        override fun areItemsTheSame(oldItem: PhotoDetails, newItem: PhotoDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoDetails, newItem: PhotoDetails): Boolean {
            return oldItem == newItem
        }
    }

    fun setItemOnClickListener(listener: itemOnClickListener){
        this.listener = listener
    }

    fun setSelected(value: Int){
        isSelected = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): photoIndicatorViewHolder {
        val holder = photoIndicatorViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.indicator_item, parent, false)
        )
        holder.itemView.item_main.setOnClickListener {
            listener.onClick(holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: photoIndicatorViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(currentList[position].thumbnailImage)
            .into(holder.itemView.item_photo)
        if (isSelected == position){
            holder.itemView.item_main.isSelected = true
            holder.itemView.item.scaleX = 1.3f
            holder.itemView.item.scaleY = 1.3f
        }else{
            holder.itemView.item_main.isSelected = false
            holder.itemView.item.scaleX = 1.0f
            holder.itemView.item.scaleY = 1.0f
        }
    }
}

class photoIndicatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

interface itemOnClickListener {
    fun onClick(value: Int)
}