package com.example.filetracker.trackfile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filetracker.database.FileDatabase
import com.example.filetracker.database.FileDatabaseDao
import com.example.filetracker.database.MovementDetail
import kotlinx.coroutines.*

class TrackFileViewModel(application: Application, private val fileId: Int): ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao: FileDatabaseDao = FileDatabase.getInstance(application).fileDatabaseDao
    private var _fileName = MutableLiveData<String?>()
    val fileName: LiveData<String?> get() = _fileName

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val file = databaseDao.getFileDetailsWithId(fileId)
                _fileName.postValue(file.fileNumber)
            }
        }
    }

    // Variable to hold the moved state
    private val _isFileMovingOut = MutableLiveData<Boolean?>(null)
    val isFileMovingOut get() = _isFileMovingOut

    fun onFileMovingOut(isMovingOUt: Boolean){
        updateDatabase(isMovingOUt)
        _isFileMovingOut.value = isMovingOUt
    }

    private fun updateDatabase(movingOUt: Boolean) {
        uiScope.launch {
            withContext(Dispatchers.IO){
                databaseDao.insertMovement(MovementDetail(movedFileId = fileId, movingOut = movingOUt))
            }
        }
    }

    fun doneNavigatingToNextFragment(){
        _isFileMovingOut.value = null
    }

}