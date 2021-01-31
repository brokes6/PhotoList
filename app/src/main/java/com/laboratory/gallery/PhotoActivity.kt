package com.laboratory.gallery

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.laboratory.gallery.databinding.ActivityPhotoBinding
import com.laboratory.gallery.self_control.itemOnClickListener
import com.laboratory.gallery.self_control.photoIndicatorAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.photo_listitem.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val IMAGE_DOWNLOAD: Int = 1;

class PhotoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPhotoBinding
    private lateinit var adapter: PhotoViewPageAdapter
    private var imgList: ArrayList<PhotoDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo)
        ImmersionBar.with(this)
            .titleBar(binding.back)
            .init()
        initData()
        initView()
    }

    private fun initData() {
        imgList = intent.getParcelableArrayListExtra("PHOTO_LIST")
        binding.bottomFunction.findViewById<TextView>(R.id.likesNum).text =
            imgList[1].like.toString()
        binding.bottomFunction.findViewById<TextView>(R.id.commentNum).text =
            imgList[1].comment.toString()
        binding.bottomFunction.findViewById<TextView>(R.id.collectionNum).text =
            imgList[1].collection.toString()
        binding.bottomFunction.findViewById<TextView>(R.id.forwardNun).text =
            imgList[1].forward.toString()
        Glide.with(this)
            .load("https://i0.hdslb.com/bfs/face/b47bdeae990a87804a0a1a3eaf042f2af40c38b0.jpg")
            .into(binding.userImg)
    }

    private fun initView() {
        adapter = PhotoViewPageAdapter(this).apply {
            binding.viewPager.adapter = this
            setLinener(object : OnMeunClickLinener {
                override fun openDialog() {
                    ChooseDialog(this@PhotoActivity).also {
                        it.setDialogOnClickListener(object : OnDialogItemClickListener {
                            override fun itemClick(position: Int) {
                                when (position) {
                                    1 -> {
                                        Toast.makeText(
                                            this@PhotoActivity,
                                            "准备分享图片",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        it.dismiss()
                                    }
                                    2 -> {
                                        DownloadImage()
                                        it.dismiss()
                                    }
                                }
                            }
                        })
                    }.show()
                }

                override fun ImageClick() {
                    if (binding.photoToolbar.visibility == View.GONE) {
                        binding.photoToolbar.visibility = View.VISIBLE
                        binding.bottomFunction.visibility = View.VISIBLE
                        binding.title.visibility = View.VISIBLE
                    } else {
                        binding.photoToolbar.visibility = View.GONE
                        binding.bottomFunction.visibility = View.GONE
                        binding.title.visibility = View.GONE
                    }

                }
            })
            submitList(imgList)
        }

        var layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val indicatorAdapter = photoIndicatorAdapter().also {
            binding.photoIndicator.adapter = it
            binding.photoIndicator.layoutManager = layoutManager
            it.setItemOnClickListener(object : itemOnClickListener {
                override fun onClick(value: Int) {
                    binding.viewPager.setCurrentItem(value, false)
                }
            })
            it.submitList(imgList)
            layoutManager.scrollToPositionWithOffset(intent.getIntExtra("PHOTO_POSITION", 0), 0)
            it.setSelected(intent.getIntExtra("PHOTO_POSITION", 0))
        }
        binding.back.setOnClickListener(this)
        binding.viewPager.setCurrentItem(intent.getIntExtra("PHOTO_POSITION", 0), false)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                layoutManager.scrollToPositionWithOffset(position, 0)
                indicatorAdapter.setSelected(binding.viewPager.currentItem)
                indicatorAdapter.notifyDataSetChanged()
            }
        })
        setMargins(binding.photoIndicator, 0, ImmersionBar.getStatusBarHeight(this), 0, 0)
    }

    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is MarginLayoutParams) {
            val p = v.layoutParams as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }

    fun DownloadImage() {
        if (Build.VERSION.SDK_INT < 29 && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                IMAGE_DOWNLOAD
            )
        } else {
            lifecycleScope.launch { saveImage() }
        }
    }

    private suspend fun saveImage() {
        withContext(Dispatchers.IO) {
            val holder =
                (binding.viewPager[0] as RecyclerView).findViewHolderForAdapterPosition(binding.viewPager.currentItem) as PhotoViewPagerHolder
            val imageUrl = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues()
            ) ?: kotlin.run {
                Toast.makeText(this@PhotoActivity, "存储失败", Toast.LENGTH_SHORT).show()
            }
            contentResolver.openOutputStream(imageUrl as Uri).use {
                if (holder.itemView.imageView.drawable.toBitmap()
                        .compress(Bitmap.CompressFormat.PNG, 90, it)
                ) {
                    MainScope().launch {
                        Toast.makeText(this@PhotoActivity, "存储成功", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    MainScope().launch {
                        Toast.makeText(this@PhotoActivity, "存储失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            IMAGE_DOWNLOAD -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    lifecycleScope.launch { saveImage() }
                } else {
                    Toast.makeText(this, "请先同意读写权限", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> {
                finish()
            }
        }
    }
}