package com.phaggu.filetracker.addFile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.phaggu.filetracker.R
import com.phaggu.filetracker.databinding.FragmentAddFileBinding
import timber.log.Timber

class AddFileFragment: Fragment() {
    private var _binding: FragmentAddFileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.i("Creating View")
        _binding = inflate(inflater, R.layout.fragment_add_file, container, false)

        val fileId = AddFileFragmentArgs.fromBundle(requireArguments()).fileId
        val application = requireNotNull(this.activity).application
        val viewModelFactory = AddFileViewModelFactory(application, fileId)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(AddFileViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Observer for navigating to next fragment
        viewModel.navigateToNextFragment.observe(viewLifecycleOwner, Observer {
            if(it == true){
                Timber.i("Navigating to List Fragment")
                this.findNavController().navigate(
                        AddFileFragmentDirections.actionAddFileFragmentToFileListFragment(false)
                )
                viewModel.doneNavigatingToNextFragment()
            }
        })

        viewModel.navigateToScanner.observe(viewLifecycleOwner, Observer {
            if(it){
                Timber.i("Navigating to Scanner")
                this.findNavController().navigate(
                        AddFileFragmentDirections.actionAddFileFragmentToBarcodeScannerFragment()
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