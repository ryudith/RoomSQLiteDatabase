package com.ryudith.roomsqlitedatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity("Player", indices = [Index("Name")])
data class PlayerEntity(
    @PrimaryKey(true)
    @ColumnInfo("Id")
    var id: Long?,

    @ColumnInfo("Name")
    var name: String,

    @ColumnInfo("CreatedAt", defaultValue = "0")
    var createdAt: Date = Date(),

    @ColumnInfo("HP", defaultValue = "100")
    var hp: Int = 100,

    @ColumnInfo("FP", defaultValue = "100")
    var fp: Int = 100,
)
