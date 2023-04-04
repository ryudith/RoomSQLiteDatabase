package com.ryudith.roomsqlitedatabase.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao
{
    @Insert(PlayerEntity::class)
    suspend fun create (player: PlayerEntity): Long

    @Query("SELECT * FROM Player WHERE Id = :id")
    suspend fun read (id: Long): PlayerEntity?

    @Update(PlayerEntity::class)
    suspend fun update (player: PlayerEntity): Int

    @Delete(PlayerEntity::class)
    suspend fun delete (player: PlayerEntity): Int

    @Query("SELECT * FROM Player ORDER BY Id ASC")
    fun browse (): Flow<List<PlayerEntity>>
}