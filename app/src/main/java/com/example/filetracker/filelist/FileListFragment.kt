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
    private var _binding: FragmentFileListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("Creating View")
        _binding = DataBindingUtil.inflate<FragmentFileListBinding>(inflater, R.layout.fragment_file_list, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}