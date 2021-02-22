package com.phaggu.filetracker

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phaggu.filetracker.database.*
import kotlinx.coroutines.*
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.lang.Exception
import java.lang.StringBuilder
import javax.xml.parsers.DocumentBuilderFactory

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val databaseDao: FileDatabaseDao = FileDatabase.getInstance(application).fileDatabaseDao

    private val _showSnackbar = MutableLiveData<Boolean>(false)
            val showSnackbar: LiveData<Boolean> get() = _showSnackbar

    fun writeData(file: File){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val files = databaseDao.getFileWithMovement()
                //val strBuilder = StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                val strBuilder = StringBuilder("")
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
                    append("</files>")

                }
                FileWriter(file, false).use { writer ->
                    writer.write("")
                    writer.write(strBuilder.toString())
                    writer.close()
                }
            }
        }


    }

    fun importData(contentResolver: ContentResolver, uri: Uri){
        uiScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    val document: Document
                    //val document = documentBuilder.parse(inputStream)
                    contentResolver.openInputStream(uri).use {
                        document = documentBuilder.parse(it)
                        val filesForDatabase = prepareDataforRoom(document)
                        insertDataIntoDatabase(filesForDatabase)
                    }



                }catch (e: Exception){

                    Timber.i("$e: ${e.message}")
                    _showSnackbar.postValue(true)
                }
            }
        }
    }

    private fun insertDataIntoDatabase(filesForDatabase: MutableList<FileMovement>) {
       TODO("Show Dialog Box informing user that present data will be deleted")
       
       TODO("Delete Database and insert new Data")
    }

    private suspend fun prepareDataforRoom(document: Document): MutableList<FileMovement> {
        val files = document.documentElement
        val fileList = files.getElementsByTagName("file")
        val filesForDatabase = mutableListOf<FileMovement>()

        // This loops through all the 'file' elements
        for(index in 0 until fileList.length){
            val file: Node = fileList.item(index)
            val fileDetail = FileDetail()
            val movementDetails = mutableListOf<MovementDetail>()

            Timber.i("${file.nodeName}[$index] reading.")
            val fileAttr: NodeList = file.childNodes

            // This loops through all the child elements of 'file'
            for(i in 0 until fileAttr.length){
                val attr: Node = fileAttr.item(i)
                Timber.i("${attr.nodeName} reading.")
                when(attr.nodeName){
                    "fileId" -> {fileDetail.fileId = attr.textContent.toInt()}
                    "fileNumber" -> {fileDetail.fileNumber = attr.textContent}
                    "fileDescription" -> {fileDetail.fileDescription = attr.textContent}
                    "movement" -> {
                        val movementAttr: NodeList = attr.childNodes
                        val movementDetail = MovementDetail(movedFileId = 0)

                        // Loops through all the child elements of 'movement' element
                        for(j in 0 until movementAttr.length){
                            val attr: Node = movementAttr.item(j)
                            when(attr.nodeName){
                                "movementId" -> {movementDetail.movementId = attr.textContent.toLong()}
                                "movedFileId" -> {movementDetail.movedFileId = attr.textContent.toInt()}
                                "movingOut" -> {movementDetail.movingOut = attr.textContent.toBoolean()}
                                "movementTime" -> {movementDetail.movementTime = attr.textContent.toLong()}
                                else -> {}
                            }
                        }
                        // All the details of this movement taken, add to list
                        movementDetails.add(movementDetail)

                    }
                    else -> {}
                    }
                //Timber.i("${attr.nodeName} of type ${attr.nodeType} with value = ${attr.textContent}")
            }
            // All the details of the file taken, add to list
            val fileMovement = FileMovement(fileDetail, movementDetails)
            filesForDatabase.add(fileMovement)
        }
        // Done with this xml file
        return filesForDatabase
    }

    fun doneShowingSnackbar(){
        _showSnackbar.value = false
    }
    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

}
