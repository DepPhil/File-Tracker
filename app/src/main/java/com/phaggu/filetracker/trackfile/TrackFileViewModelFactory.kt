package com.phaggu.filetracker.trackfile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TrackFileViewModelFactory(private val application: Application, private val fileId: Int): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackFileViewModel::class.java)) {
            return TrackFileViewModel(application, fileId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}