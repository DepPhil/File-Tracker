package com.example.filetracker.filelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.filetracker.R
import com.example.filetracker.databinding.FragmentFileListBinding
import timber.log.Timber

class FileListFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("Creating View")
        val binding = DataBindingUtil.inflate<FragmentFileListBinding>(inflater, R.layout.fragment_file_list, container, false)
        return binding.root
    }
}