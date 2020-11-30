package com.example.filetracker.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface FileDatabaseDao {
    @Query("SELECT COUNT(file_id) FROM file_detail_table")
    fun getInFileNumber(): LiveData<Int?>

    @Query("SELECT COUNT(file_id) FROM file_detail_table")
    fun getOutFileNumber(): LiveData<Int?>

    @Query("SELECT * FROM max_movement_time")
    fun getMaxMovementTime(): List<MaxMovementTime>

    @Insert
    fun insertFile(file: FileDetail)

    @Query("SELECT * FROM file_detail_table WHERE file_id = :fileId")
    fun getFileDetailsWithId(fileId: Int): FileDetail

    @Query("DELETE FROM file_detail_table WHERE file_id = :fileId")
    fun deleteFile(fileId: Long)

    // Insert Movement details
    @Insert
    fun insertMovement(movement: MovementDetail)

//    @Insert
//    fun insertFileMovement(fileMovement: FileMovement)

    //
    @Query("SELECT * FROM file_detail_table")
    fun getFileDetails(): List<FileDetail>

    @Transaction
    @Query("SELECT * FROM file_detail_table")
    fun getFileWithMovement(): List<FileMovement>



    @Transaction
    @Query("SELECT * FROM file_detail_table WHERE file_id = :fileId")
    fun getFileMovementWithID(fileId:Long): FileMovement
    // Get the maximum id number in order to create a new id number of file
    @Query("SELECT MAX(file_id) FROM file_detail_table")
    fun getMaxFileId(): Int

//    @Query("SELECT * FROM movement_detail_table WHERE movement_time = (SELECT MAX(movement_time) FROM movement_detail_table)" +
//            "INNER JOIN file_detail_table ON moved_file_id = file_id")
//    fun getAllFiles()

//    @Query("DELETE FROM file_detail_table WHERE file_id = :fileId")
//    fun deleteFileDetails(fileId: Long)

}
