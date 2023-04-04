package com.ryudith.roomsqlitedatabase.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ryudith.roomsqlitedatabase.model.PlayerDao
import com.ryudith.roomsqlitedatabase.model.PlayerEntity
import com.ryudith.roomsqlitedatabase.utility.RoomDateToLongConverter

@Database(entities = [PlayerEntity::class], version = 3, exportSchema = true, autoMigrations = [AutoMigration(1, 2)])
@TypeConverters(RoomDateToLongConverter::class)
abstract class GameDb: RoomDatabase()
{
    abstract fun player (): PlayerDao

    companion object
    {
        @Volatile
        private var instance: GameDb? = null
        private var simpleLock = Any()
        private val dbName = "GameDatabase.db"

        operator fun invoke (context: Context): GameDb
        {
            if (instance == null)
            {
                synchronized(simpleLock) {
                    instance = Room.databaseBuilder(context, GameDb::class.java, dbName)
                        .addMigrations(MIGRATION_2_3)
                        .build()
                }
            }

            return instance!!
        }

        val MIGRATION_2_3 = object: Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Player RENAME TO PlayerTemp")
                database.execSQL("DROP INDEX IF EXISTS index_Player_Name")
//                database.execSQL("DROP TABLE IF EXISTS Player")  // wrong, existing data will gone

                database.execSQL("CREATE TABLE IF NOT EXISTS `Player` (" +
                        "`Id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "`Name` TEXT NOT NULL, " +
                        "`CreatedAt` INTEGER NOT NULL DEFAULT 0, " +
                        "`HP` INTEGER NOT NULL DEFAULT 100," +
                        "`FP` INTEGER NOT NULL DEFAULT 100" +
                        ")")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_Player_Name` ON `Player` (`Name`)")
                database.execSQL("INSERT INTO Player (Id, Name, CreatedAt) SELECT Id, Name, CreatedAt FROM PlayerTemp")
                database.execSQL("DROP TABLE IF EXISTS PlayerTemp")
            }
        }
    }
}







