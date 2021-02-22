package com.phaggu.filetracker.addFile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phaggu.filetracker.database.FileDatabase
import com.phaggu.filetracker.database.FileDatabaseDao
import com.phaggu.filetracker.database.FileDetail
import com.phaggu.filetracker.database.MovementDetail
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*

class AddFileViewModel(application: Application, private val fileId: Int): ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao: FileDatabaseDao

    // To navigate to next fragment
    private val _navigateToNextFragment = MutableLiveData<Boolean?>(null)
    val navigateToNextFragment: LiveData<Boolean?> get() = _navigateToNextFragment

    private val _navigateToScanner = MutableLiveData<Boolean>(false)
    val navigateToScanner: LiveData<Boolean>get() = _navigateToScanner

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
            val file = FileDetail(fileId, fileNumber!!.toUpperCase(Locale.ROOT), fileDescription!!)
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
        _navigateToScanner.value = false
    }

    fun onNavigateToScanner(){
        _navigateToScanner.value = true
    }
    override fun onCleared() {
        super.onCleared()
    }
}