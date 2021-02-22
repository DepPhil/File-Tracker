package com.phaggu.filetracker.addFile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddFileViewModelFactory(private val application: Application, private val fileId: Int): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFileViewModel::class.java)) {
            return AddFileViewModel(application, fileId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}