package com.example.filetracker.filelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
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
        val application = requireNotNull(this.activity).application
        val showOutList = FileListFragmentArgs.fromBundle(requireArguments()).showOutList
        val viewModelFactory = FileListViewModelFactory(application, showOutList)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(FileListViewModel::class.java)
        binding.viewModel = viewModel

        // Set up Adapter
        val adopter = FileListAdopter(FileListClickListener {
            fileId -> viewModel.onItemClick(fileId)
        })
        binding.fileListRecyclerView.adapter = adopter

        // Observe the List
        viewModel.filesWithLastMovement.observe(viewLifecycleOwner, Observer {
            if(it != null){
                adopter.submitList(it)
            }
        })

        // Observe the click on item
        viewModel.navigateToNextFragment.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(
                        FileListFragmentDirections.actionFileListFragmentToFileDetailFragment(it)
                )
                viewModel.doneNavigatingToNextFragment()
            }
        })

        viewModel.navigateToScanner.observe(viewLifecycleOwner, Observer {
            if(it){
                Timber.i("Navigating to Scanner")
                this.findNavController().navigate(
                        FileListFragmentDirections.actionFileListFragmentToBarcodeScannerFragment()
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