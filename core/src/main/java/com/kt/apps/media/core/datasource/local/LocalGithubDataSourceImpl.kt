package com.kt.apps.media.core.datasource.local

import android.util.Log
import com.kt.apps.media.core.Constants
import com.kt.apps.media.core.datasource.IGithubDataSource
import com.kt.apps.media.core.models.GithubRepoDTO
import com.kt.apps.media.core.models.GithubUserDTO
import com.kt.apps.media.core.storage.database.dao.GithubReposDao
import com.kt.apps.media.core.storage.keyvalue.IKeyValueStorage
import com.kt.apps.media.core.utils.mapper.toGitHubRepo
import com.kt.apps.media.core.utils.mapper.toGithubRepoDTO
import javax.inject.Inject

class LocalGithubDataSourceImpl @Inject constructor(
    private val githubReposDao: GithubReposDao,
    private val keyValueStorage: IKeyValueStorage
) : IGithubDataSource {
    override suspend fun fetchUserInfo(name: String): Result<GithubUserDTO> {
        return try {
            val user = keyValueStorage.get(name, type = GithubUserDTO::class.java)!!
            Result.success(user)
        } catch (e: NullPointerException) {
            Result.failure(e)
        }
    }

    override suspend fun loadItemForPage(
        userName: String,
        page: Int,
        perPage: Int
    ): Result<List<GithubRepoDTO>> {
        return githubReposDao.loadItems(page * perPage, perPage)
            .map {
                it.toGithubRepoDTO()
            }
            .let {
                Result.success(it)
            }
    }

    override suspend fun saveItems(item: List<GithubRepoDTO>) {
        githubReposDao.insertItems(item.map {
            it.toGitHubRepo()
        })
    }

    override suspend fun saveUserInfo(name: String, userResponse: GithubUserDTO) {
        keyValueStorage.save(name, userResponse, GithubUserDTO::class.java)
        keyValueStorage.setLong(Constants.KEY_LAST_UPDATE_USER_TIME, System.currentTimeMillis())
    }
}