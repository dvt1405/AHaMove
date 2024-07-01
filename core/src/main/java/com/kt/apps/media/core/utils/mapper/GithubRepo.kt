package com.kt.apps.media.core.utils.mapper

import com.kt.apps.media.core.api.respose.GithubRepoItem
import com.kt.apps.media.core.models.GithubRepoDTO
import com.kt.apps.media.core.storage.database.dbmodels.GithubRepo


fun GithubRepoItem.toGithubRepoDTO(): GithubRepoDTO {
    return GithubRepoDTO(
        id = id,
        name = name,
        fullName = fullName,
        private = isPrivate,
        size = size,
        stargazersCount = stargazersCount,
        watchersCount = watchersCount,
        language = language,
        forksCount = forksCount,
        openIssueCount = openIssuesCount,
        url = url,
        description = description
    )
}

fun GithubRepoDTO.toGitHubRepo(): GithubRepo {
    return GithubRepo(
        id = id,
        name = name,
        fullName = fullName,
        private = private,
        size = size,
        stargazersCount = stargazersCount,
        watchersCount = watchersCount,
        language = language,
        forksCount = forksCount,
        openIssueCount = openIssueCount,
        url = url,
        description = description
    )
}

fun GithubRepo.toGithubRepoDTO() = GithubRepoDTO(
    id = id,
    name = name,
    fullName = fullName,
    private = private,
    size = size,
    stargazersCount = stargazersCount,
    watchersCount = watchersCount,
    language = language,
    forksCount = forksCount,
    openIssueCount = openIssueCount,
    url = url,
    description = description
)