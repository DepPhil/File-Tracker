package com.example.filetracker

import android.os.Bundle
import android.Manifest
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.filetracker.barcodescanner.AddNewFileDialogFragment
import com.example.filetracker.camera.Camera
import timber.log.Timber
import java.io.File


class MainActivity: AppCompatActivity(){

       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_main)
           val viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

           // Navigation UI
           val navController = findNavController(R.id.nav_host_fragment)
           val appBarConfiguration = AppBarConfiguration(navController.graph)
           val toolbar = findViewById<Toolbar>(R.id.toolbar)

           with(toolbar){
               setupWithNavController(navController, appBarConfiguration)
               setOnMenuItemClickListener {
                   it?.let {
                       if(it.itemId == R.id.aboutFragment){
                           navController.navigate(R.id.aboutFragment)

                       }else if(it.itemId == R.id.exportData){
                           sendIntent(viewModel)
                       }

                   }
                   true
               }
           }



           // Check if camera permissions has been granted. If not, seek permissions
           Camera.checkForPermission(baseContext)
           if(Camera.permissionGranted != true){
               Camera.requestForPermission(this)
           }
    }

    private fun sendIntent(viewModel: MainActivityViewModel) {
        val dir = applicationContext.filesDir
        val file = File("$dir/xml")
        file.mkdir()
        val xmlFile = File("$file/data.xml")
        val res = xmlFile.createNewFile()
        val uri = FileProvider.getUriForFile(applicationContext, "com.example.myapp.fileprovider", xmlFile)
        viewModel.writeData(xmlFile)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/xml"
            data = uri
            clipData = ClipData.newRawUri("", uri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivityForResult(Intent.createChooser(shareIntent, null), 11)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
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