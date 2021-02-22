package com.phaggu.filetracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import timber.log.Timber

@Database(
        entities = [FileDetail::class, MovementDetail::class],
        views = [MaxMovementTime::class, FileDetailWithLastMovement::class],
        version = 5,
        exportSchema = true)
abstract class FileDatabase: RoomDatabase() {

    abstract val fileDatabaseDao: FileDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: FileDatabase? = null

        fun getInstance(context: Context): FileDatabase{
            Timber.i("Getting Instance of Database")
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            FileDatabase::class.java,
                            "file_movement_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}