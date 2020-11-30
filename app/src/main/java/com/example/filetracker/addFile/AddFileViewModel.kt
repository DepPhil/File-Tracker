package com.example.filetracker.addFile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filetracker.database.FileDatabase
import com.example.filetracker.database.FileDatabaseDao
import com.example.filetracker.database.FileDetail
import com.example.filetracker.database.MovementDetail
import kotlinx.coroutines.*
import timber.log.Timber

class AddFileViewModel(application: Application, private val fileId: Int): ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao: FileDatabaseDao

    // To navigate to next fragment
    private val _navigateToNextFragment = MutableLiveData<Boolean?>(null)
    val navigateToNextFragment: LiveData<Boolean?> get() = _navigateToNextFragment

    // Variables to get info from editTexts
    var fileNumber: String? = null
    var fileDescription: String? = null
    init {
        Timber.i("Initialising ViewModel")
        databaseDao = FileDatabase.getInstance(application).fileDatabaseDao
    }

    fun onAddFileRequest(){
        Timber.i("Adding File %s F.No. %s, Description: %s", fileId,  fileNumber, fileDescription)
        if(fileNumber != null && fileDescription != null){
            val file = FileDetail(fileId, fileNumber!!, fileDescription!!)
            uiScope.launch {
                withContext(Dispatchers.IO){
                    databaseDao.insertFile(file)
                    databaseDao.insertMovement(MovementDetail(movedFileId = fileId))
                }
            }
            _navigateToNextFragment.value = true
        }

    }

    fun doneNavigatingToNextFragment(){
        _navigateToNextFragment.value = false
    }
    override fun onCleared() {
        super.onCleared()
    }
}