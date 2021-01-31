package com.laboratory.gallery.self_control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.laboratory.gallery.R
import com.laboratory.gallery.databinding.FragmentAutoLoadBinding

class AutoLoadFragment : Fragment() {
    private lateinit var binding: FragmentAutoLoadBinding
    private lateinit var mAdapter: AutoLoadAdapter
    private val galleryViewModel by viewModels<AutoLoadViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auto_load, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        mAdapter = AutoLoadAdapter(requireActivity())
        binding.AutoRecyclerLayout.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = mAdapter
        }
        galleryViewModel.pagedPhotoLiveData.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }
}