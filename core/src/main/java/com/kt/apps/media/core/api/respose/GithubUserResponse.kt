package com.kt.apps.media.core.api.respose

import com.google.gson.annotations.SerializedName

data class GithubUserResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("blog")
    val blog: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("followers")
    val followers: Long,
    @SerializedName("following")
    val following: Int,
    @SerializedName("hooks_url")
    val hooksUrl: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_verified")
    val isVerified: Boolean,
    @SerializedName("issues_url")
    val issuesUrl: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("members_url")
    val membersUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("node_id")
    val nodeId: String,
    @SerializedName("public_repos")
    val publicRepos: Int,
    @SerializedName("repos_url")
    val reposUrl: String,
    @SerializedName("twitter_username")
    val twitterUsername: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("url")
    val url: String
)