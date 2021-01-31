package com.laboratory.gallery.smart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.laboratory.gallery.GalleryCellAdapter
import com.laboratory.gallery.GalleryViewModel
import com.laboratory.gallery.R
import com.laboratory.gallery.databinding.FragmentGalleryBinding
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader

class GalleryFragment : Fragment() {
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var mAdapter: GalleryCellAdapter
    private val galleryViewModel by viewModels<GalleryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        mAdapter = GalleryCellAdapter(requireActivity())
        binding.recyclerLayout.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = mAdapter
        }
        galleryViewModel.photoListLive.observe(this, Observer {
            mAdapter.submitList(it)
        })
        binding.swipeRefreshLayout.setRefreshHeader(ClassicsHeader(context))
        binding.swipeRefreshLayout.setRefreshFooter(ClassicsFooter(context))

        binding.swipeRefreshLayout.setOnRefreshListener {
            galleryViewModel.apply {
                resetQuery()
            }
            binding.swipeRefreshLayout.finishRefresh()
        }

        binding.swipeRefreshLayout.setOnLoadMoreListener {
            galleryViewModel.apply {
                fetData()
            }
            binding.swipeRefreshLayout.finishLoadMore()
        }

        galleryViewModel.photoListLive.value ?: galleryViewModel.resetQuery()
    }
}