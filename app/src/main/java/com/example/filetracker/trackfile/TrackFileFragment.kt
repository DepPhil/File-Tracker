package com.example.filetracker.trackfile

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
import com.example.filetracker.databinding.FragmentMainBinding
import com.example.filetracker.databinding.FragmentTrackFileBinding
import timber.log.Timber

class TrackFileFragment: Fragment() {
    private var _binding:FragmentTrackFileBinding? = null
    private val binding get() = _binding!!
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_track_file, container, false)
        val application = requireNotNull(this.activity).application
        val fileId = TrackFileFragmentArgs.fromBundle(requireArguments()).fileId
        val viewModelFactory = TrackFileViewModelFactory(application, fileId)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(TrackFileViewModel::class.java)
        binding.viewModel = viewModel

        // Observer for IN/OUT click event
        viewModel.isFileMovingOut.observe(viewLifecycleOwner, Observer {
            if(it != null){
                Timber.i("File is moving In/Out")
                Timber.i("Navigating to List Fragment")
                this.findNavController().navigate(
                        TrackFileFragmentDirections.actionTrackFileFragmentToFileListFragment(it)
                )
                viewModel.doneNavigatingToNextFragment()
            }
        })

        viewModel.navigateToScanner.observe(viewLifecycleOwner, Observer {
            if(it == true){
                Timber.i("Navigating to Scanner")
                this.findNavController().navigate(
                        TrackFileFragmentDirections.actionTrackFileFragmentToBarcodeScannerFragment()
                )
                viewModel.doneNavigatingToNextFragment()
            }
        })
        binding.lifecycleOwner = this
        return binding.root
    }
}