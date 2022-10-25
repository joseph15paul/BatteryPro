package com.example.batterypro

import androidx.room.*

@Entity(tableName = "batteryStats_table")
data class batteryStats (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "batteryLevel") val batteryLevel: Float?,
    @ColumnInfo(name = "charging") val charging: Boolean,
    @ColumnInfo(name = "timestamp") val timestamp: String

    )
