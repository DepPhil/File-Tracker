package com.example.filetracker.filedetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filetracker.database.FileDatabase
import com.example.filetracker.database.MovementDetail
import kotlinx.coroutines.*

class FileDetailViewModel(application: Application, fileId: Int): ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao= FileDatabase.getInstance(application).fileDatabaseDao

    private val _movementDetailList = MutableLiveData<List<MovementDetail?>>()
            val movementDetailList: LiveData<List<MovementDetail?>> get() = _movementDetailList

    private val _fileName = MutableLiveData<String?>()
    val fileName: LiveData<String?> get() = _fileName

    private val _fileDescription = MutableLiveData<String?>()
    val fileDescription: LiveData<String?> get() = _fileDescription

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                _movementDetailList.postValue(
                        databaseDao.getListMovementDetails(fileId)
                )
                val file = databaseDao.getFileDetailsWithId(fileId)
                _fileName.postValue(file.fileNumber)
                _fileDescription.postValue(file.fileDescription)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}