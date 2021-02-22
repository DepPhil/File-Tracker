package com.phaggu.filetracker.filelist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phaggu.filetracker.database.FileDatabase
import com.phaggu.filetracker.database.FileDetailWithLastMovement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber

class FileListViewModel(application: Application, showOutList: Boolean): ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao= FileDatabase.getInstance(application).fileDatabaseDao

    private val _filesWithLastMovement: LiveData<List<FileDetailWithLastMovement>> = databaseDao.getAllFilesWithLastMovement(showOutList)
     val filesWithLastMovement: LiveData<List<FileDetailWithLastMovement>> get() = _filesWithLastMovement

    private val _navigateToNextFragment = MutableLiveData<Int?>()
    val navigateToNextFragment: LiveData<Int?> get() = _navigateToNextFragment

    private val _navigateToScanner = MutableLiveData<Boolean>(false)
    val navigateToScanner: LiveData<Boolean>get() = _navigateToScanner

    init {
        Timber.i("Initialising ViewModel")


    }

    fun onItemClick(fileId: Int){
        _navigateToNextFragment.value = fileId
    }
    fun doneNavigatingToNextFragment(){
        _navigateToNextFragment.value = null
        _navigateToScanner.value = false
    }

    fun onNavigateToScanner(){
        _navigateToScanner.value = true
    }

}