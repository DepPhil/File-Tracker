package com.example.filetracker.mainFragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.filetracker.R
import com.example.filetracker.camera.Camera
import com.example.filetracker.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

// This is the main fragment from where our app will start
class MainFragment: Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("Creating View")
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        //val application = requireNotNull(this.activity).application
        //val dataSource = FileDatabase.getInstance(application).fileDatabaseDao

        val mainFragmentViewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        binding.mainFragmentViewModel = mainFragmentViewModel
        // Handling request to show list of files
        mainFragmentViewModel.showOutList.observe(viewLifecycleOwner, Observer {
            if(it != null){
                Timber.i("Navigating to List Fragment")
                this.findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToFileListFragment(it)
                )
                mainFragmentViewModel.doneNavigationToListFragment()
            }
        })

        // Navigate to Barcode Scanner Fragment
        mainFragmentViewModel.navigateToBarcodeScanner.observe(viewLifecycleOwner, Observer {
            if(it){
                if(Camera.permissionGranted == true){
                    Timber.i("Navigating to Barcode Fragment")
                    this.findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToBarcodeScannerFragment()
                    )
                    mainFragmentViewModel.doneNavigationToBarcodeScanner()
                }else{
                    Snackbar.make(
                        binding.root, "You Need to grand Camera Permission to use this feature", Snackbar.LENGTH_SHORT
                    ).show()
                }




            }
        })

        // Set File Numbers in the fragment
        mainFragmentViewModel.inFilesNumber.observe(viewLifecycleOwner, Observer {
            //binding.inFileNumber.text = it.toString()
        })
        mainFragmentViewModel.outFilesNumber.observe(viewLifecycleOwner, Observer {
           binding.outFileNumber.text = it.toString()
        })

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


