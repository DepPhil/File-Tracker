package com.example.filetracker.barcodescanner

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.filetracker.R
import com.example.filetracker.camera.Camera
import com.example.filetracker.databinding.FragmentBarcodeScannerBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_barcode_scanner.*
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeScannerFragment: Fragment(), AddNewFileDialogFragment.NoticeDialogListener {
    private var _binding: FragmentBarcodeScannerBinding? = null
    private val binding get() = _binding!!
    // Create executor for camera
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var barcodeScannerViewModel: BarcodeScannerViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("Creating View")
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_barcode_scanner,container, false
        )

        // Setup ViewModel
        val application = requireNotNull(this.activity).application
        val viewModelFactory = BarcodeScannerViewModelFactory(application)
        barcodeScannerViewModel =
            ViewModelProvider(this, viewModelFactory).get(BarcodeScannerViewModel::class.java)
        binding.barcodeScannerViewModel = barcodeScannerViewModel


       // Listener for barcodeString for navigating to add file fragment
        barcodeScannerViewModel.barcodeStringForAddingFile.observe(viewLifecycleOwner, Observer {
            if(it != null){

                val bundle = Bundle()
                bundle.putInt("fileId", it)
                val dialogFragment = AddNewFileDialogFragment()
                        dialogFragment.setTargetFragment(this, 100)
                        dialogFragment.arguments = bundle
                        dialogFragment.show(this.parentFragmentManager, "addFile")
                barcodeScannerViewModel.doneNavigatingToNextFragment()
            }
        })

        // Listner for barcode String for navigating to track file fragment
        barcodeScannerViewModel.barcodeStringForTrackingFile.observe(viewLifecycleOwner, Observer {
            if(it != null){
                Camera.stopCamera()
                Timber.i("Navigating to Track File Fragment")
                this.findNavController().navigate(
                        BarcodeScannerFragmentDirections.actionBarcodeScannerFragmentToTrackFileFragment(it)
                )
                barcodeScannerViewModel.doneNavigatingToNextFragment()
            }
        })

        // Start Camera Executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Starting the Camera

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Camera.startCamera(
            requireNotNull(this.activity).applicationContext,
            this,
            previewView,
            cameraExecutor,
            barcodeScannerViewModel
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Timber.i("Navigating to Add File Fragment")
        val fileId:Int? = dialog.arguments?.getInt("fileId")
        this.findNavController().navigate(
                BarcodeScannerFragmentDirections.actionBarcodeScannerFragmentToAddFileFragment(requireNotNull(fileId))
        )
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Timber.i("Navigating back to Main Fragment")
    }

//    private fun startCamera(){
//        Timber.i("Starting Camera")
//        val cameraProviderFuture =
//            ProcessCameraProvider.getInstance(requireNotNull(this.activity).applicationContext)
//
//        cameraProviderFuture.addListener(Runnable {
//            // Used to bind the lifecycle of cameras to the lifecycle owner
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            // Preview
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(previewView.surfaceProvider)
//                }
//
//            // Select back camera as a default
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            try {
//                // Unbind use cases before rebinding
//                cameraProvider.unbindAll()
//
//                // Bind use cases to camera
//                cameraProvider.bindToLifecycle(
//                    viewLifecycleOwner, cameraSelector, preview)
//
//            } catch(exc: Exception) {
//                Timber.e(exc, "Use case binding failed")
//            }
//
//        }, ContextCompat.getMainExecutor(requireNotNull(this.activity).applicationContext))
//    }
}

class AddNewFileDialogFragment: DialogFragment(){
    // Use this instance of the interface to deliver action events
    private lateinit var listener: NoticeDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

//    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            listener = context as NoticeDialogListener
//        } catch (e: ClassCastException) {
//            // The activity doesn't implement the interface, throw exception
//            throw ClassCastException((context.toString() +
//                    " must implement NoticeDialogListener"))
//        }
//    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Timber.i("Creating Dialong Fragment")
        return activity?.let {
            listener = this.targetFragment as NoticeDialogListener
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("This File does not exist. Add this file?")
                .setPositiveButton("Add",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogPositiveClick(this)
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onDialogNegativeClick(this)
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    //return super.onCreateDialog(savedInstanceState)
}