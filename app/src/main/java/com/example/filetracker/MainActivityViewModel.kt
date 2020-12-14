package com.example.filetracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.filetracker.database.FileDatabase
import com.example.filetracker.database.FileDatabaseDao
import kotlinx.coroutines.*
import java.io.File
import java.io.FileWriter
import java.lang.StringBuilder

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao: FileDatabaseDao = FileDatabase.getInstance(application).fileDatabaseDao

    fun writeData(file: File){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val files = databaseDao.getFileWithMovement()
                val strBuilder = StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                with(strBuilder){
                    append("<files>\n")
                    for (file in files) {
                        append("<file>\n")
                        append("<fileId>${file.file.fileId}</fileId>\n")
                        append("<fileNumber>${file.file.fileNumber}</fileNumber>\n")
                        append("<fileDescription>${file.file.fileDescription}</fileDescription>\n")
                            for(movement in file.movement){
                                append("<movement>\n")
                                    append("<movementId>${movement.movementId}</movementId>\n")
                                    append("<movedFileId>${movement.movedFileId}</movedFileId>\n")
                                    append("<movingOut>${movement.movingOut}</movingOut>\n")
                                    append("<movementTime>${movement.movementTime}</movementTime>\n")
                                append("</movement>\n")
                            }
                        append("</file>\n")
                    }
                    append("</files>\n")

                }
                FileWriter(file, false).use { writer ->
                    writer.write("")
                    writer.write(strBuilder.toString())
                    writer.close()
                }
            }
        }


    }

}
