package com.ryudith.roomsqlitedatabase.utility

import androidx.room.TypeConverter
import java.util.Date

object RoomDateToLongConverter
{
    @TypeConverter
    fun fromTimestampToDate (value: Long?): Date
    {
        if (value == null)
        {
            return Date()
        }

        return Date(value)
    }

    @TypeConverter
    fun fromDateToTimestamp (value: Date?): Long
    {
        if (value == null)
        {
            return 0L
        }

        return value.time
    }
}