package com.example.filetracker.filedetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filetracker.database.FileDatabase
import com.example.filetracker.database.FileMovement
import com.example.filetracker.database.MovementDetail
import kotlinx.coroutines.*

class FileDetailViewModel(private val application: Application, private val fileId: Int): ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao= FileDatabase.getInstance(application).fileDatabaseDao

    private val _movementDetailList = MutableLiveData<List<MovementDetail?>>()
            val movementDetailList: LiveData<List<MovementDetail?>> get() = _movementDetailList

    private val _fileName = MutableLiveData<String?>()
    val fileName: LiveData<String?> get() = _fileName

    private val _fileDescription = MutableLiveData<String?>()
    val fileDescription: LiveData<String?> get() = _fileDescription

    private val _navigateToScanner = MutableLiveData<Boolean>(false)
    val navigateToScanner: LiveData<Boolean>get() = _navigateToScanner

    private val _fileMovement = MutableLiveData<FileMovement?>(null)
    val fileMovement: LiveData<FileMovement?>get() = _fileMovement

    private val _navigateToEditFileFragment = MutableLiveData<Int?>(null)
    val navigateToEditFileFragment: LiveData<Int?>get() = _navigateToEditFileFragment

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                _fileMovement.postValue(databaseDao.getFileMovementWithID(fileId))
                _movementDetailList.postValue(
                        databaseDao.getListMovementDetails(fileId)
                )
                val file = databaseDao.getFileDetailsWithId(fileId)
                _fileName.postValue(file.fileNumber)
                _fileDescription.postValue(file.fileDescription)
            }
        }
    }

    fun doneNavigatingToNextFragment(){
        _navigateToScanner.value = false
        _navigateToEditFileFragment.value = null
    }

    fun onNavigationToScanner(){
        _navigateToScanner.value = true
    }

    fun onNavigateToEditFileFragment(){
        _navigateToEditFileFragment.value = fileId
    }

    fun getString( number: Int?): String? {
        return number.toString()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}