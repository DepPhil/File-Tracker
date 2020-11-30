package com.example.filetracker

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.filetracker.database.*
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.filetracker", appContext.packageName)
    }


}

@RunWith(AndroidJUnit4::class)
class DatabaseTests {
    private lateinit var dao: FileDatabaseDao
    private lateinit var db: FileDatabase

    @Before
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, FileDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        dao = db.fileDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun addFile(){
        val file = FileDetail(1)
        val movement = MovementDetail(movedFileId = 0)
        val movementList = listOf<MovementDetail>(movement)
        val fileMovement = FileMovement(file, movementList)

        dao.insertFile(file)
        //val fm = dao.getFileMovementWithID(0)
        val fl = dao.getFileDetailsWithId(0)
        dao.deleteFile(0)
        assertEquals(0, fl.fileId)


    }


}