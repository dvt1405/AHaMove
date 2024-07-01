package com.kt.apps.media.core.repository

import com.kt.apps.media.core.Constants
import com.kt.apps.media.core.datasource.IGithubDataSource
import com.kt.apps.media.core.datasource.INetworkCheckDataSource
import com.kt.apps.media.core.di.qualifiers.GithubDataSourceQualifier
import com.kt.apps.media.core.models.GithubRepoDTO
import com.kt.apps.media.core.models.GithubUserDTO
import com.kt.apps.media.core.storage.keyvalue.IKeyValueStorage
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    @GithubDataSourceQualifier(GithubDataSourceQualifier.Type.Network)
    private val networkDataSource: IGithubDataSource,
    @GithubDataSourceQualifier(GithubDataSourceQualifier.Type.Local)
    private val localDataSource: IGithubDataSource,
    private val networkCheck: INetworkCheckDataSource,
    private val keyValueStorage: IKeyValueStorage
) : IGithubRepository {
    private var currentItems: MutableList<GithubRepoDTO> = mutableListOf()
    private var canLoadMore: Boolean = true

    override suspend fun getInfo(name: String): GithubUserDTO {
        val localRs = localDataSource.fetchUserInfo(name)
        if (localRs.isSuccess) {
            return localRs.getOrThrow()
        }
        val networkRs = networkDataSource.fetchUserInfo(name)
        if (networkRs.isSuccess) {
            val userInfo = networkRs.getOrThrow()
            localDataSource.saveUserInfo(userInfo)
        }
        return networkRs.getOrThrow()
    }

    override suspend fun updateUserInfoIfNeeded(name: String) {
        val lastUpdateTime = keyValueStorage.getLong(Constants.KEY_LAST_UPDATE_USER_TIME, 0L)
        if (System.currentTimeMillis() - lastUpdateTime < Constants.UPDATE_THRESH_HOLD_DEF) {
            return
        }
        val networkRs = networkDataSource.fetchUserInfo(name)
        if (networkRs.isSuccess) {
            val userInfo = networkRs.getOrThrow()
            localDataSource.saveUserInfo(userInfo)
        }
    }

    override suspend fun loadItemForPage(
        name: String,
        page: Int,
        perPage: Int
    ): List<GithubRepoDTO> {
        val startIndex = page * perPage
        if (startIndex < currentItems.size) {
            val endIndex = (page + 1) * perPage
            return if (endIndex > currentItems.size) {
                currentItems.subList(startIndex, currentItems.size)
            } else {
                currentItems.subList(startIndex, endIndex)
            }
        }
        if (!canLoadMore) {
            throw IllegalStateException("Can't load more")
        }
        val localRs = localDataSource.loadItemForPage(name, page, perPage)
        if (localRs.isSuccess && !localRs.getOrNull().isNullOrEmpty()) {
            val listItem = localRs.getOrThrow()
            currentItems.addAll(listItem)
            return listItem
        }
        val networkRs = networkDataSource.loadItemForPage(name, page, perPage)
        if (networkRs.isSuccess && networkRs.getOrNull().isNullOrEmpty()) {
            canLoadMore = false
        }
        if (networkRs.isSuccess) {
            val listItem = networkRs.getOrThrow()
            localDataSource.saveItems(listItem)
            currentItems.addAll(listItem)
            return listItem
        }
        return networkRs.getOrThrow()
    }

    override fun canLoadMore(): Boolean {
        return canLoadMore
    }
}