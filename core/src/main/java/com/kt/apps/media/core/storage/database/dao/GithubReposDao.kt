package com.kt.apps.media.core.storage.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kt.apps.media.core.storage.database.dbmodels.GithubRepo

@Dao
abstract class GithubReposDao {

    @Query("Select * from GithubRepo")
    abstract suspend fun getAll(): List<GithubRepo>

    @Query("Select * from GithubRepo limit :perPage offset :offset")
    abstract suspend fun loadItems(offset: Int, perPage: Int): List<GithubRepo>

    @Query("Delete from GithubRepo")
    abstract suspend fun deleteAllItems()

    @Delete
    abstract suspend fun deleteItems(item: GithubRepo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updateItem(githubRepo: GithubRepo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertItems(listItem: List<GithubRepo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertItem(item: GithubRepo)

}