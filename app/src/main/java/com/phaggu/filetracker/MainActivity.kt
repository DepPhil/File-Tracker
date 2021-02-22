package com.phaggu.filetracker

import android.os.Bundle
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.phaggu.filetracker.camera.Camera
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.io.File

private const val INTENT_SHARE_FILE = 11
private const val INTENT_GET_FILE = 12

class MainActivity: AppCompatActivity(){
       private lateinit var viewModel: MainActivityViewModel
       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_main)
           viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

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
                           sendIntent()
                       }else if(it.itemId == R.id.importData){
                           getData()
                       }

                   }
                   true
               }
           }

           viewModel.showSnackbar.observe(this, Observer {
               if(it){
                   Snackbar.make(findViewById(R.id.nav_host_fragment), R.string.incorrect_data_file, Snackbar.LENGTH_LONG)
                           .show()
                   viewModel.doneShowingSnackbar()
               }
           })


           // Check if camera permissions has been granted. If not, seek permissions
           Camera.checkForPermission(baseContext)
           if(Camera.permissionGranted != true){
               Camera.requestForPermission(this)
           }
    }

    private fun getData() {
        val getDataIntent = Intent().apply {
            action = Intent.ACTION_OPEN_DOCUMENT
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        Timber.i("Starting activity for result with code: $INTENT_GET_FILE")
        startActivityForResult(getDataIntent, INTENT_GET_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.i("Got activity with result code: $requestCode")
        if(requestCode == INTENT_GET_FILE && resultCode == Activity.RESULT_OK){

            try{
                val fileUri: Uri = requireNotNull(data?.data)
//                TODO("content resolver taking too much time to provide input stream" +
//                        "find a way to make it short.")
                //val inputStream = contentResolver.openInputStream(fileUri)
                viewModel.importData(contentResolver, fileUri)


            }catch (e: Exception){
                Timber.i("$e: ${e.message}")
                // Create Snackbar
                Snackbar.make(findViewById(R.id.nav_host_fragment), R.string.incorrect_data_file, Snackbar.LENGTH_LONG)
                        .show()

            }






            Timber.i("Got the document")

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun sendIntent() {
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
        startActivityForResult(Intent.createChooser(shareIntent, null), INTENT_SHARE_FILE)
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