package com.phaggu.filetracker.mainFragment

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.phaggu.filetracker.R
import com.phaggu.filetracker.camera.Camera
import com.phaggu.filetracker.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

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

        mainFragmentViewModel.buttonClicked.observe(viewLifecycleOwner, Observer { it ->

            if(it == true){
                val database = context?.getDatabasePath("file_movement_database")


                if(false){


                }
                //val getDir = context?.getDir(dir.toString() + "/xml", Context.MODE_PRIVATE)
//                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
//                    addCategory(Intent.CATEGORY_OPENABLE)
//                    type = "application/db"
//                }
//                startActivityForResult(intent, 1)
                mainFragmentViewModel.doneWithButtonClick()
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 11){

        }

        if(false){
            val database = context?.getDatabasePath("file_movement_database")
            val path = database?.absolutePath

            val inputStream = FileInputStream(path)

            val contentResolver = this.context?.contentResolver
            val uri = data?.let {
                it.data
            }
            if (uri != null) {
                // create a file with this uri
                val path = uri.path!!
                val file = File(path)
                var database: SQLiteDatabase
                try {
                    database = SQLiteDatabase.openOrCreateDatabase(path, null)
                } catch (e: SQLiteException){
                    Timber.i("Cannot open database: $e")
                }

                if (contentResolver != null) {
                    contentResolver.openFileDescriptor(uri, "w")?.use { descriptor ->
                        FileOutputStream(descriptor.fileDescriptor).use {
                            var data: Int? = 0
                            var i: Long = 0L
                            while (true){
                                data = inputStream.read()
                                if(data == -1) {break}
                                it.write(data)
                                i++
                            }
                            Timber.i("i: $i")
                        }
//                    Timber.i("Files has been created: $uri")
                    }
                }
        }





        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


