package com.phaggu.filetracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FileDatabaseDao {
    @Query("SELECT COUNT(file_id) FROM filedetailwithlastmovement WHERE moving_out = 0")
    fun getInFileNumber(): LiveData<Int?>

    @Query("SELECT COUNT(file_id) FROM filedetailwithlastmovement WHERE moving_out = 1")
    fun getOutFileNumber(): LiveData<Int?>

    @Query("SELECT * FROM filedetailwithlastmovement WHERE moving_out = :showOutList ORDER BY movement_time DESC")
    fun getAllFilesWithLastMovement(showOutList: Boolean): LiveData<List<FileDetailWithLastMovement>>

    @Query("SELECT * FROM movement_detail_table WHERE moved_file_id = :fileId ORDER BY movement_time DESC")
    fun getListMovementDetails(fileId: Int): List<MovementDetail>

    @Query("SELECT * FROM filedetailwithlastmovement WHERE file_id = :fileId")
    fun getFileWithLastMovement(fileId: Int): FileDetailWithLastMovement

    @Query("DELETE FROM movement_detail_table")
    fun deleteAllMovements()

    @Query("DELETE FROM file_detail_table")
    fun deleteAllfiles()

    @Insert
    fun insertFile(file: FileDetail)

    @Insert
    fun insertMovement(movement: MovementDetail)

    @Query("SELECT COUNT(file_id) FROM file_detail_table")
    fun getFileCount(): Int

    @Query("SELECT COUNT(movement_id) FROM movement_detail_table")
    fun getMovementCount():Int


//    @Query("SELECT * FROM max_movement_time")
//    fun getMaxMovementTime(): List<MaxMovementTime>



    @Query("SELECT * FROM file_detail_table WHERE file_id = :fileId")
    fun getFileDetailsWithId(fileId: Int): FileDetail

    @Query("DELETE FROM file_detail_table WHERE file_id = :fileId")
    fun deleteFile(fileId: Long)



    @Update
    fun upDateFile(file: FileDetail)

//    @Insert
//    fun insertFileMovement(fileMovement: MutableList<FileMovement>)

    //
    @Query("SELECT * FROM file_detail_table")
    fun getFileDetails(): List<FileDetail>

    @Transaction
    @Query("SELECT * FROM file_detail_table")
    fun getFileWithMovement(): List<FileMovement>



    @Transaction
    @Query("SELECT * FROM file_detail_table WHERE file_id = :fileId")
    fun getFileMovementWithID(fileId:Int): FileMovement

    // Get the maximum id number in order to create a new id number of file
    @Query("SELECT MAX(file_id) FROM file_detail_table")
    fun getMaxFileId(): Int?

//    @Query("SELECT * FROM movement_detail_table WHERE movement_time = (SELECT MAX(movement_time) FROM movement_detail_table)" +
//            "INNER JOIN file_detail_table ON moved_file_id = file_id")
//    fun getAllFiles()

//    @Query("DELETE FROM file_detail_table WHERE file_id = :fileId")
//    fun deleteFileDetails(fileId: Long)

}

