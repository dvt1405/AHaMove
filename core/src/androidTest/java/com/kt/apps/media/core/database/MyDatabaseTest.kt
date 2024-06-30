package com.kt.apps.media.core.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kt.apps.media.core.api.respose.GithubRepoItem
import com.kt.apps.media.core.storage.database.MyDatabase
import com.kt.apps.media.core.storage.database.dao.GithubReposDao
import com.kt.apps.media.core.storage.database.dbmodels.GithubRepo
import com.kt.apps.media.core.utils.mapper.toGitHubRepo
import com.kt.apps.media.core.utils.mapper.toGithubRepoDTO
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class MyDatabaseTest {
    private lateinit var githubRepoDao: GithubReposDao
    private lateinit var dbTest: MyDatabase
    private lateinit var listDataTest: List<GithubRepo>

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val json = context.assets.open("sample_data_test.json")
            .bufferedReader()
            .readText()
        listDataTest = Gson().fromJson<List<GithubRepoItem>>(
            json,
            object : TypeToken<List<GithubRepoItem>>() {}.type
        ).map {
            it.toGithubRepoDTO()
                .toGitHubRepo()
        }
        dbTest = Room.inMemoryDatabaseBuilder(
            context, MyDatabase::class.java
        ).build()
        githubRepoDao = dbTest.githubReposDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dbTest.close()
    }

    @Test
    fun testWriteAndRead() = runTest {
        githubRepoDao.insertItems(listDataTest)
        assert(
            githubRepoDao.getAll()
                .map {
                    it.id
                }
                .contains(1936771)
        )
        githubRepoDao.deleteAllItems()
        assert(githubRepoDao.getAll().isEmpty())
    }

}