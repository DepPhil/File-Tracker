package com.example.filetracker

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
//import androidx.databinding.library.BuildConfig
import timber.log.Timber

class FileTrackerApplication:  Application(){

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())

        }
        Timber.i("Timber is logging")
    }




}
