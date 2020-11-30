package com.example.filetracker.camera

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.*
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.filetracker.barcodescanner.BarcodeScannerViewModel
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

object Camera
{
    private val cameraPermission = arrayOf(Manifest.permission.CAMERA)
    private var _permissionGranted: Boolean? = null
    private lateinit var cameraProvider: ProcessCameraProvider
    val permissionGranted: Boolean?  get() = _permissionGranted

     fun requestForPermission(activity: Activity){
        Timber.i("Requesting for Permissions")
        ActivityCompat.requestPermissions(
            activity, cameraPermission,10
        )
    }

     fun checkForPermission(baseContext: Context){
        Timber.i("Checking for Permissions")
        _permissionGranted = cameraPermission.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
        Timber.i("Permission checked. It is %s", _permissionGranted)
    }

    fun onRequestPermissionResult(requestCode: Int){
        Timber.i("onRequestPermissionResult called with request code %s", requestCode)

    }

    fun stopCamera(){
        cameraProvider.unbindAll()
    }
    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        cameraExecutor: ExecutorService,
        barcodeScannerViewModel: BarcodeScannerViewModel
        ) {
        Timber.i("Starting Camera")
        val cameraProviderFuture =
                ProcessCameraProvider.getInstance(context)


        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
            Timber.i("Preveiwview %s %s", previewView.width, previewView.height)

            val imageCapture = ImageCapture.Builder()
                    .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyzer(barcodeScannerViewModel))
                    }

            // Set View Port
            //val aspectRatio = Rational(92, 80)

//            @androidx.camera.core.ExperimentalUseCaseGroup
//            val viewport = ViewPort.Builder(aspectRatio, preview.targetRotation).build()
//
//            @androidx.camera.core.ExperimentalUseCaseGroup
//            val useCaseGroup = UseCaseGroup.Builder()
//                    .addUseCase(preview)
//                    .addUseCase(imageAnalyzer)
//                    .addUseCase(imageCapture)
//                    .setViewPort(viewport)
//                    .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                Timber.i("Binding Camera to lifecycleOwner")


                val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageAnalyzer, imageCapture)
                //initialiseAutoFocus(camera, context)

            } catch(exc: Exception) {
                Timber.e(exc, "Use case binding failed")


            }



        }, ContextCompat.getMainExecutor(context))

    }

    private fun initialiseAutoFocus(camera: Camera, context: Context) {
        val cameraControl = camera.cameraControl
        val factory = SurfaceOrientedMeteringPointFactory(1.0F, 1.0F)
        val point = factory.createPoint(0.5f, 0.5f)
        val point2 = factory.createPoint(0.8f, 0.8f)
        val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                .addPoint(point2, FocusMeteringAction.FLAG_AE) // could have many
                // auto calling cancelFocusAndMetering in 5 seconds
                .setAutoCancelDuration(5, TimeUnit.SECONDS)
                .build()

        val future = cameraControl.startFocusAndMetering(action)
        future.addListener( Runnable {
            val result = future.get()
            // process the result
        } , ContextCompat.getMainExecutor(context))
    }

}

 private class BarcodeAnalyzer(val barcodeScannerViewModel: BarcodeScannerViewModel) : ImageAnalysis.Analyzer {

    override fun analyze(imageProxy: ImageProxy) {
        Timber.i("analyze called")

        val barcodeScannerOptions = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        @androidx.camera.core.ExperimentalGetImage
        val mediaImage = imageProxy.image

        @androidx.camera.core.ExperimentalGetImage
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            // ...
            val scanner = BarcodeScanning.getClient(barcodeScannerOptions)
            val result = scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        // Task completed successfully
                        // ...
                        Timber.i("barcode scanning success")
                        for(barcode in barcodes){
                            Timber.i("Bardcode raw data is: %s", barcode.rawValue)
                            barcodeScannerViewModel.gotBarcodeString(barcode.rawValue)
                        }
                    }
                    .addOnFailureListener {
                        // Task failed with an exception
                        // ...
                        Timber.e(it, "Barcode scanning failed")
                    }
                    .addOnCompleteListener {
                        Timber.i("Barcode scanning complete")
                        imageProxy.close()
                    }
        }
    }
}