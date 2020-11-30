package com.example.filetracker.database

import androidx.room.*
import java.util.*

@Entity(tableName = "file_detail_table")
data class FileDetail(

        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "file_id")
        var fileId: Int = 0,

        @ColumnInfo(name = "file_number")
        var fileNumber: String = "F.No.II(3)12/Estt/FileTracker/2020",

        @ColumnInfo(name = "file_description")
        var fileDescription: String = "Description of File Tracker App used for tracking files - reg."
)

@Entity(tableName = "movement_detail_table")
data class MovementDetail(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "movement_id")
        var movementId: Long = 0L,

        @ColumnInfo(name = "moved_file_id")
        var movedFileId: Int,

        @ColumnInfo(name = "moving_out") // If the file is not moving out, then it is moving in
        var movingOut: Boolean = false,

        @ColumnInfo(name="movement_time")
        var movementTime: Long = System.currentTimeMillis()
)


data class FileMovement(
        @Embedded
        var file: FileDetail,


        @Relation(
        parentColumn = "file_id",
        entityColumn = "moved_file_id"
        )
        var movement: List<MovementDetail> = emptyList()

)

@DatabaseView("SELECT MAX(movement_time) as movement_time, moved_file_id, moving_out FROM movement_detail_table GROUP BY moved_file_id", viewName = "max_movement_time")
data class MaxMovementTime(
        @ColumnInfo(name = "movement_time")
        val movementTime: Long,

        @ColumnInfo(name = "moved_file_id")
        val movedFileId: Int,

        @ColumnInfo(name = "moving_out")
        val movingOut: Boolean
)

@DatabaseView("SELECT file_id, file_number, file_description, movement_time, moving_out FROM file_detail_table INNER JOIN max_movement_time ON file_id = moved_file_id")
data class FileDetailWithLastMovement(
        @ColumnInfo(name = "file_id") val fileId: Int,
        @ColumnInfo(name = "file_number") val fileNumber: String,
        @ColumnInfo(name = "file_description") val fileDescription: String,
        @ColumnInfo(name = "movement_time") val movementTime: Long,
        @ColumnInfo(name = "moving_out") val movingOut: Boolean

)