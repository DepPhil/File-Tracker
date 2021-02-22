package com.phaggu.filetracker.barcodescanner

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BarcodeScannerViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BarcodeScannerViewModel::class.java)) {
            return BarcodeScannerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}