package com.example.filetracker.filedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.filetracker.R
import com.example.filetracker.databinding.FragmentFileDetailsBinding

class FileDetailFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentFileDetailsBinding>(inflater, R.layout.fragment_file_details, container, false)
        return binding.root
    }
}