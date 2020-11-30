package com.example.filetracker.addFile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.filetracker.R
import com.example.filetracker.databinding.FragmentAddFileBinding
import com.example.filetracker.databinding.FragmentBarcodeScannerBinding
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
            }
        })
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}