package com.example.filetracker.mainFragment

import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider.getUriForFile
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.filetracker.R
import com.example.filetracker.camera.Camera
import com.example.filetracker.database.FileDatabase
import com.example.filetracker.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter

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
                val dir = context?.filesDir
                val file = File(dir.toString() + "/xml")
                file.mkdir()
                val xmlFile = File("$file/data.xml")
                val res = xmlFile.createNewFile()
                val uri = getUriForFile(requireContext(), "com.example.myapp.fileprovider", xmlFile)

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/xml"
                    data = uri
                    clipData = ClipData.newRawUri("", uri)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    putExtra(Intent.EXTRA_STREAM, uri)
                }
                startActivityForResult(Intent.createChooser(shareIntent, null), 11)

                if(false){
                    val fileWriter = FileWriter(xmlFile).use { writer ->
                        writer.write("I'm writing something")
                        writer.close()
                    }

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


