package com.example.batterypro

import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface batteryStatsDao {

        @Query("SELECT * FROM batteryStats_table")
        fun getStats(): Flow<List<batteryStats>>

        @Query("SELECT * FROM batteryStats_table")
        fun getStatsTillNow(): List<batteryStats>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insert(stats: batteryStats): Void

        @Query("DELETE FROM batteryStats_table")
        suspend fun deleteAll(): Void
    }
