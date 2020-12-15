package com.example.filetracker.editfile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filetracker.addFile.AddFileViewModel
import com.example.filetracker.database.FileDetail

class EditFileViewModelFactory(private val application: Application, private val fileId: Int): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditFileViewModel::class.java)) {
            return EditFileViewModel(application, fileId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}