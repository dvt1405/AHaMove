package com.kt.apps.media.core.models

data class GithubRepoDTO(
    val id: Int,
    val name: String,
    val description: String?,
    val fullName: String,
    val private: Boolean,
    val size: Int,
    val stargazersCount: Long,
    val watchersCount: Int,
    val language: String?,
    val forksCount: Long,
    val openIssueCount: Int,
    val url: String
) {
}