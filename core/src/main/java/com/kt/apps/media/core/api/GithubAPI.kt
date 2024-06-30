package com.kt.apps.media.core.api

import com.kt.apps.media.core.api.respose.GithubRepoItem
import com.kt.apps.media.core.api.respose.GithubUserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubAPI {

    @GET("orgs/{name}")
    suspend fun getUserInfo(@Path("name") name: String = "google"): GithubUserResponse

    @GET("orgs/{name}/repos")
    suspend fun getGithubRepos(
        @Path("name") name: String = "google",
        @Query("page") page: Int = 0,
        @Query("per_page") perPage: Int = 20
    ): List<GithubRepoItem>

    companion object {
        const val BASE_URL = "https://api.github.com"
    }
}