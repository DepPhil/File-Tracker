package com.phaggu.filetracker.mainFragment

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phaggu.filetracker.database.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.io.File

class MainFragmentViewModel(application: Application): AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao: FileDatabaseDao

    // Implement better ways to link to database
    private   val _inFilesNumber: LiveData<Int?>
    val inFilesNumber: LiveData<Int?> get() = _inFilesNumber

    private val _outFilesNumber : LiveData<Int?>
            val outFilesNumber: LiveData<Int?> get() = _outFilesNumber

    // A button click listener
    private val _buttonClicked = MutableLiveData<Boolean?>()
    val buttonClicked: LiveData<Boolean?> get() = _buttonClicked

    init {
        Timber.i("Initialising Class")
        databaseDao = FileDatabase.getInstance(application).fileDatabaseDao
        _inFilesNumber = databaseDao.getInFileNumber()
        _outFilesNumber = databaseDao.getOutFileNumber()
        //Timber.i("inFiles %s", _inFilesNumber.value)
//        uiScope.launch {
//
////            withContext(Dispatchers.IO){
////                Timber.i("Fetching Files from Database")
////                Timber.i("File numbers are: %s", _inFilesNumber.value)
//////                databaseDao.insertFile(FileDetail(0))
//////                databaseDao.insertMovement(MovementDetail(movedFileId = 0))
////                //val fileMovement: FileMovement = databaseDao.getFileMovementWithID(0)
////                //val files = databaseDao.getAllFiles()
////                //Timber.i("filemovement is")
////            }
//        }


    }



    // A variable to store the state of event for showing IN/OUT list
    private val _showOutList = MutableLiveData<Boolean?>()
    val showOutList: LiveData<Boolean?> get() = _showOutList
    fun doneNavigationToListFragment(){ _showOutList.value = null}

    fun requstForList(showOutList: Boolean){
        _showOutList.value = showOutList
    }

    // A variable to store state of scan button
    private val _navigateToBarcodeScanner = MutableLiveData(false)
    val navigateToBarcodeScanner: LiveData<Boolean> get() = _navigateToBarcodeScanner
    fun requestForNavigationToBarcodeScanner(){
        Timber.i("request for navigating to barcode scanner")
        _navigateToBarcodeScanner.value = true
    }
    fun doneNavigationToBarcodeScanner(){_navigateToBarcodeScanner.value = false}

    // A simple function to add some data to the database
    fun addData(){
        uiScope.launch {
            withContext(Dispatchers.IO){

                val file1 = FileDetail(1, "II(3)1/Estt/Testing/2020", "A file to keep test something")
                val file2 = FileDetail(2, "II(3)2/Estt/Main/2020", "The Main Fragment is starting point of our app")
                val file3 = FileDetail(3, "II(3)3/Estt/Scanner/2020", "The Barcode Scanner uses MI kit of google")
                with(databaseDao){
                    insertFile(file1)
                    insertFile(file2)
                    insertFile(file3)
                    insertMovement(MovementDetail(movedFileId = 1))
                    insertMovement(MovementDetail(movedFileId = 2))
                    insertMovement(MovementDetail(movedFileId = 3))
                }

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun showPath(){
        //val file = Environment.getDataDirectory()
        val file = File("/data")
        val fileList = file.listFiles()
        Timber.i("The root directory is: ${file.absolutePath}")
    }

    fun createFile(){
        _buttonClicked.value = true

    }
    fun doneWithButtonClick(){
        _buttonClicked.value = null
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun showData(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val files = databaseDao.getFileWithMovement()
                Timber.i("The files are: %s", files)

                val allFiles = JSONArray()

                for(file in files){
                    val jsonFile = JSONObject()
                    val movements = JSONArray()
                    for(movement in file.movement){
                        val jsonObject = JSONObject()
                        with(jsonObject){
                            put("movementId", movement.movementId)
                            put("movedFileId", movement.movedFileId)
                            put("movingOut", movement.movingOut)
                            put("movementTime", movement.movementTime)
                        }
                        movements.put(jsonObject)
                    }
                    with(jsonFile){
                        put("fileId", file.file.fileId)
                        put("fileDescription", file.file.fileDescription)
                        put("fileNumber", file.file.fileNumber)
                        put("movements", movements)
                    }
                    allFiles.put(jsonFile)
                }
                Timber.i("All files are: %s", allFiles.toString(1))
                //Timber.i(Environment.getStorageDirectory().absolutePath)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}