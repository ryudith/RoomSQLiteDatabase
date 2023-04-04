package com.ryudith.roomsqlitedatabase.repository

import com.ryudith.roomsqlitedatabase.data.GameDb
import com.ryudith.roomsqlitedatabase.model.PlayerDao
import com.ryudith.roomsqlitedatabase.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

class PlayerRepository (db: GameDb)
{
    private val playerDao: PlayerDao
    init {
        playerDao = db.player()
    }

    suspend fun create (player: PlayerEntity): Long
    {
        return playerDao.create(player)
    }

    suspend fun read (id: Long): PlayerEntity?
    {
        return playerDao.read(id)
    }

    suspend fun update (player: PlayerEntity): Int
    {
        return playerDao.update(player)
    }

    suspend fun delete (player: PlayerEntity): Int
    {
        return playerDao.delete(player)
    }

    fun browse (): Flow<List<PlayerEntity>>
    {
        return playerDao.browse()
    }
}




