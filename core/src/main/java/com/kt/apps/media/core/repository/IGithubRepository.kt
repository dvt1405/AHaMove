package com.kt.apps.media.core.repository

import com.kt.apps.media.core.models.GithubRepoDTO
import com.kt.apps.media.core.models.GithubUserDTO

interface IGithubRepository {
    suspend fun getInfo(name: String): GithubUserDTO
    suspend fun updateUserInfoIfNeeded(name: String)
    fun canLoadMore(): Boolean
    suspend fun loadItemForPage(
        name: String,
        page: Int,
        perPage: Int
    ): List<GithubRepoDTO>
}