package com.kt.apps.media.core.utils.mapper

import com.kt.apps.media.core.api.respose.GithubUserResponse
import com.kt.apps.media.core.models.GithubUserDTO


fun GithubUserResponse.toGithubUserDTO(): GithubUserDTO {
    return GithubUserDTO(
        avatarUrl,
        blog,
        createdAt,
        description,
        email,
        followers,
        following,
        hooksUrl,
        htmlUrl,
        id,
        isVerified,
        issuesUrl,
        location,
        login,
        membersUrl,
        name,
        nodeId,
        publicRepos,
        reposUrl,
        twitterUsername,
        type,
        updatedAt,
        url
    )
}
