package com.phaggu.filetracker.editfile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditFileViewModelFactory(private val application: Application, private val fileId: Int): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditFileViewModel::class.java)) {
            return EditFileViewModel(application, fileId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}