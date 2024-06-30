package com.kt.apps.media.core.storage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kt.apps.media.core.storage.database.dao.GithubReposDao
import com.kt.apps.media.core.storage.database.dbmodels.GithubRepo

@Database(
    autoMigrations = [],
    exportSchema = true,
    version = 1,
    entities = [GithubRepo::class]
)
abstract class MyDatabase : RoomDatabase() {

    abstract fun githubReposDao(): GithubReposDao

    companion object {
        private const val DATABASE_NAME = "MyDatabase"

        @Volatile
        private var INSTANCE: MyDatabase? = null

        @Synchronized
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(context, MyDatabase::class.java, DATABASE_NAME)
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}