package com.example.filetracker.filedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.filetracker.R
import com.example.filetracker.databinding.FragmentFileDetailsBinding
import com.example.filetracker.databinding.FragmentFileListBinding
import timber.log.Timber

class FileDetailFragment: Fragment() {
    private var _binding: FragmentFileDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_file_details, container, false)

        val application = requireNotNull(this.activity).application
        val fileId = FileDetailFragmentArgs.fromBundle(requireArguments()).fileId
        val viewModelFactory = FileDetailViewModelFactory(application, fileId)
        val viewModel  = ViewModelProvider(this, viewModelFactory)
                .get(FileDetailViewModel::class.java)
        binding.viewModel = viewModel

        // Setup adapter
        val adapter = FileDetailAdapter()
        binding.fileDetailRecyclerView.adapter = adapter

        // Observer for submitting list to the adapter
        viewModel.movementDetailList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToScanner.observe(viewLifecycleOwner, Observer {
            if (it){
                Timber.i("Navigating to Scanner")
                this.findNavController().navigate(
                        FileDetailFragmentDirections.actionFileDetailFragmentToBarcodeScannerFragment()
                )
                viewModel.doneNavigatingToNextFragment()
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}