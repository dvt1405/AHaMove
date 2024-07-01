package com.kt.apps.media.core.models

data class GithubUserDTO(
    val avatarUrl: String,
    val blog: String,
    val createdAt: String,
    val description: String,
    val email: String,
    val followers: Long,
    val following: Int,
    val hooksUrl: String,
    val htmlUrl: String,
    val id: Int,
    val isVerified: Boolean,
    val issuesUrl: String,
    val location: String,
    val login: String,
    val membersUrl: String,
    val name: String,
    val nodeId: String,
    val publicRepos: Int,
    val reposUrl: String,
    val twitterUsername: String,
    val type: String,
    val updatedAt: String,
    val url: String
) {
}
