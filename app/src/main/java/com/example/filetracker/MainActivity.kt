package com.example.filetracker

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.filetracker.barcodescanner.AddNewFileDialogFragment
import com.example.filetracker.camera.Camera
import timber.log.Timber


class MainActivity: AppCompatActivity(){

       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_main)

           // Navigation UI
           val navController = findNavController(R.id.nav_host_fragment)
           val appBarConfiguration = AppBarConfiguration(navController.graph)
           findViewById<Toolbar>(R.id.toolbar)
                   .setupWithNavController(navController, appBarConfiguration)

           // Check if camera permissions has been granted. If not, seek permissions
           Camera.checkForPermission(baseContext)
           if(Camera.permissionGranted != true){
               Camera.requestForPermission(this)
           }
    }

    // This function is called when permission results are returned by the system
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 10){
            Camera.checkForPermission(baseContext)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}