package com.laboratory.gallery

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.photo_listitem.view.*
import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.progressmanager.body.ProgressInfo


class PhotoViewPageAdapter(private val context: Context) :
    ListAdapter<PhotoDetails, PhotoViewPagerHolder>(diff) {

    private lateinit var linener: OnMeunClickLinener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewPagerHolder {
        val holder = PhotoViewPagerHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.photo_listitem, parent, false)
        )
        holder.itemView.imageView.setOnLongClickListener {
            linener.openDialog()
            return@setOnLongClickListener true
        }
        holder.itemView.imageView.setOnClickListener {
            linener.ImageClick()
        }
        return holder
    }

    override fun onBindViewHolder(holder: PhotoViewPagerHolder, position: Int) {
        holder.itemView.mainProgressBar.visibility = View.VISIBLE
        ChangeImage(holder)
        Glide.with(holder.itemView).load(getItem(position).masterMap)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.itemView.mainProgressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.itemView.mainProgressBar.visibility = View.GONE
                    return false
                }

            })
            .into(holder.itemView.imageView)
    }

    fun setLinener(linener: OnMeunClickLinener) {
        this.linener = linener
    }

    fun ChangeImage(holder: PhotoViewPagerHolder) {
        ProgressManager.getInstance().addResponseListener(
            getItem(holder.adapterPosition).masterMap,
            object : ProgressListener {
                override fun onProgress(progressInfo: ProgressInfo?) {
                    progressInfo?.let {
                        holder.itemView.mainProgressBar.setAnimProgress(it.percent)
//                        holder.itemView.mainProgressBar.setmText("${it.percent}%")
                        Log.e("TAG", "onProgress: 当前进度为${it.percent}")
                        if (it.isFinish) {
                            holder.itemView.mainProgressBar.visibility = View.GONE
                        }
                    }
                }

                override fun onError(id: Long, e: Exception?) {
                    holder.itemView.mainProgressBar.visibility = View.GONE
                    Toast.makeText(context, "下载错误", Toast.LENGTH_SHORT).show()
                }
            })
        holder.itemView.mainProgressBar.visibility = View.VISIBLE
    }


    object diff : DiffUtil.ItemCallback<PhotoDetails>() {
        override fun areItemsTheSame(oldItem: PhotoDetails, newItem: PhotoDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoDetails, newItem: PhotoDetails): Boolean {
            return oldItem == newItem
        }

    }

    private fun OpenPopupMenu(v: View) {
        PopupMenu(context, v).also { pp ->
            pp.menuInflater.inflate(R.menu.menu, pp.menu)
            pp.show()
            pp.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Download -> {
                        linener.openDialog()
                    }
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

}

class PhotoViewPagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

interface OnMeunClickLinener {
    fun openDialog()
    fun ImageClick()
}