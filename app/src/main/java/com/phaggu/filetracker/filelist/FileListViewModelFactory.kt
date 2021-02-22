package com.phaggu.filetracker.filelist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FileListViewModelFactory(private val application: Application, private val showOUtList: Boolean): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FileListViewModel::class.java)) {
            return FileListViewModel(application, showOUtList) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}