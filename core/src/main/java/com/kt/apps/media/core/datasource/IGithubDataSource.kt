package com.kt.apps.media.core.datasource

import com.kt.apps.media.core.models.GithubRepoDTO
import com.kt.apps.media.core.models.GithubUserDTO

interface IGithubDataSource {
    suspend fun fetchUserInfo(name: String): Result<GithubUserDTO>
    suspend fun loadItemForPage(userName: String, page: Int, perPage: Int): Result<List<GithubRepoDTO>>
    suspend fun saveItems(item: List<GithubRepoDTO>)
    suspend fun saveUserInfo(name: String, userResponse: GithubUserDTO)
}