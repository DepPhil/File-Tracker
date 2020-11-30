package com.example.filetracker.barcodescanner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filetracker.camera.Camera
import com.example.filetracker.database.FileDatabase
import com.example.filetracker.database.FileDatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.NumberFormatException

class BarcodeScannerViewModel(application: Application): AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao: FileDatabaseDao = FileDatabase.getInstance(application).fileDatabaseDao
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    // A variable to hold barcode data for moving to tracking file
    private val _barcodeStringForTrackingFile = MutableLiveData<Int?>(null)
    val barcodeStringForTrackingFile: LiveData<Int?> get() = _barcodeStringForTrackingFile

    // A variable to hold barcode data for moving to tracking file
    private val _barcodeStringForAddingFile = MutableLiveData<Int?>(null)
    val barcodeStringForAddingFile: LiveData<Int?> get() = _barcodeStringForAddingFile

    fun gotBarcodeString(barcodeString: String?){
        val fileId: Int? = validateBarcodeString(barcodeString)
        if(fileId != null){
            Camera.stopCamera()
            checkDatabase(fileId)
        }

    }

    private fun checkDatabase(fileId: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val file = databaseDao.getFileDetailsWithId(fileId)
                if(file != null){
                    Timber.i("File Already exists, move to track Fragment")
                    _barcodeStringForTrackingFile.postValue(fileId)
                }else{
                    Timber.i("File doesnot exist. Add File")
                    _barcodeStringForAddingFile.postValue(fileId)
                }
            }
        }
    }

    private fun validateBarcodeString(barcodeString: String?): Int? {
        return try{
            barcodeString?.toInt()
        } catch (e: NumberFormatException){
            null
        }
    }

    fun doneNavigatingToNextFragment(){
        _barcodeStringForTrackingFile.value = null
        _barcodeStringForAddingFile.value = null
    }


}