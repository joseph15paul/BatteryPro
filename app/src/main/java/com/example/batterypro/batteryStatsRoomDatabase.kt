package com.example.batterypro

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(batteryStats::class), version = 1, exportSchema = false)
public abstract class batteryStatsRoomDatabase : RoomDatabase() {


        abstract fun batteryStatsDao(): batteryStatsDao

    private class BatteryStatsDatabaseCallback( private val scope: CoroutineScope ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.batteryStatsDao())
                }
            }
        }


        suspend fun populateDatabase(batteryDao: batteryStatsDao) {
            // Delete all content here.
            batteryDao.deleteAll()


        }
    }
        companion object {
            // Singleton prevents multiple instances of database opening at the
            // same time.
            @Volatile
            private var INSTANCE: batteryStatsRoomDatabase? = null

            fun getDatabase(
                context: BatteryStatsApplication,
                scope: CoroutineScope
            ): batteryStatsRoomDatabase {
                // if the INSTANCE is not null, then return it,
                // if it is, then create the database
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        batteryStatsRoomDatabase::class.java,
                        "batteryStats_database"
                    ).addCallback(BatteryStatsDatabaseCallback(scope)).build()
                    INSTANCE = instance
                    // return instance
                    instance
                }
            }
        }
    }
