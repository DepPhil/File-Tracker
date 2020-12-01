package com.example.filetracker.filelist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.filetracker.database.FileDatabase
import com.example.filetracker.database.FileDatabaseDao
import com.example.filetracker.database.FileDetailWithLastMovement
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

    init {
        Timber.i("Initialising ViewModel")


    }

    fun onItemClick(fileId: Int){
        Timber.i("An item clicked with id: $fileId")
    }

}