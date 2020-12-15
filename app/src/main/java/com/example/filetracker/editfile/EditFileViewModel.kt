package com.example.filetracker.editfile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filetracker.database.FileDatabase
import com.example.filetracker.database.FileDatabaseDao
import com.example.filetracker.database.FileDetail
import kotlinx.coroutines.*

class EditFileViewModel(private val application: Application, private val fileId: Int): ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao: FileDatabaseDao = FileDatabase.getInstance(application).fileDatabaseDao

     val fileNumber = MutableLiveData<String?>(null)
     val fileDescription = MutableLiveData<String?>(null)

    private val _navigateToNextFragment = MutableLiveData<Int?>(null)
    val navigateToNextFragment: LiveData<Int?>get() = _navigateToNextFragment

    init {
        uiScope.launch {
            withContext(Dispatchers.IO){
                val file = databaseDao.getFileDetailsWithId(fileId)
                fileNumber.postValue(file.fileNumber)
                fileDescription.postValue(file.fileDescription)
            }
        }
    }
    fun onSaveChanges(){

        saveFileToDatabase()

    }

    private fun saveFileToDatabase() {
        if(fileNumber.value != null && fileDescription.value != null){
            val file = FileDetail(fileId, fileNumber.value!!, fileDescription.value!!)
            uiScope.launch {
                withContext(Dispatchers.IO){
                    databaseDao.upDateFile(file)
                }
            }
            _navigateToNextFragment.value = fileId
        }
    }

    fun onDoneNavigationToNextFragment(){
        _navigateToNextFragment.value=null
    }
}