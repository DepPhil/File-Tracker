package com.example.filetracker.filedetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filetracker.filelist.FileListViewModel

class FileDetailViewModelFactory(private val application: Application, private val fileId: Int)
    : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FileDetailViewModel::class.java)) {
            return FileDetailViewModel(application, fileId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}