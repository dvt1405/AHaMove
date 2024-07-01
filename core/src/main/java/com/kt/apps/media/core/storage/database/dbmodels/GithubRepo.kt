package com.kt.apps.media.core.storage.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GithubRepo")
data class GithubRepo(
    @PrimaryKey
    var id: Int = 0,
    var name: String = "",
    var fullName: String = "",
    var private: Boolean = false,
    var size: Int = -1,
    var stargazersCount: Long = -1L,
    var watchersCount: Int = -1,
    var language: String? = null,
    var forksCount: Long = -1L,
    var openIssueCount: Int = -1,
    var url: String = "",
    var description: String? = null
) {
}