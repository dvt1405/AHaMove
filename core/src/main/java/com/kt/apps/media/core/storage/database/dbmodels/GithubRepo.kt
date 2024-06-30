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
    var stargazersCount: Int = -1,
    var watchersCount: Int = -1,
    var language: String = "",
    var forksCount: Int = -1,
    var openIssueCount: Int = -1,
    var url: String = ""
) {
}