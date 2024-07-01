package com.kt.apps.media.core.datasource.network

import com.kt.apps.media.core.api.GithubAPI
import com.kt.apps.media.core.api.respose.GithubUserResponse
import com.kt.apps.media.core.datasource.IGithubDataSource
import com.kt.apps.media.core.exceptions.CusException
import com.kt.apps.media.core.models.GithubRepoDTO
import com.kt.apps.media.core.models.GithubUserDTO
import com.kt.apps.media.core.datasource.INetworkCheckDataSource
import com.kt.apps.media.core.exceptions.mappingErrorCode
import com.kt.apps.media.core.utils.ErrorCode
import com.kt.apps.media.core.utils.mapper.toGithubRepoDTO
import com.kt.apps.media.core.utils.mapper.toGithubUserDTO
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkGithubDataSourceImpl @Inject constructor(
    private val _api: GithubAPI,
    private val networkCheck: INetworkCheckDataSource
) : IGithubDataSource {

    override suspend fun fetchUserInfo(name: String): Result<GithubUserDTO> {
        val githubUser = fetchUserInfoAndRetry()
            .map {
                it.toGithubUserDTO()
            }
        if (githubUser.isSuccess) {
            return githubUser
        }
        val throwableMapping = githubUser.mappingErrorCode()
        return Result.failure(throwableMapping)
    }

    private suspend fun fetchUserInfoAndRetry(
        retryTimes: Int = RETRY_TIMES_DEF,
        retryInMilli: Long? = null
    ): Result<GithubUserResponse> {
        if (!networkCheck.checkIsNetworkOnline()) {
            return Result.failure(
                CusException(
                    ErrorCode.ERROR_NO_NETWORK,
                    "Network offline"
                )
            )
        }
        val result = if (retryInMilli != null) {
            withTimeoutOrNull(retryInMilli) {
                kotlin.runCatching {
                    _api.getUserInfo()
                }
            }
        } else {
            runCatching {
                _api.getUserInfo()
            }
        }

        if (result!!.isSuccess) {
            return result
        }

        if (retryTimes > 0) {
            return fetchUserInfoAndRetry(
                retryTimes - 1,
                retryInMilli = retryInMilli
            )
        }

        return result
    }

    override suspend fun loadItemForPage(
        userName: String,
        page: Int,
        perPage: Int
    ): Result<List<GithubRepoDTO>> {
        val result = retryLoadItemForPage(userName, page, perPage)
        if (result.isSuccess) {
            return result
        }
        val throwableMapping = result.mappingErrorCode()
        return Result.failure(throwableMapping)
    }

    private suspend fun retryLoadItemForPage(
        userName: String,
        page: Int,
        perPage: Int,
        retryTimes: Int = RETRY_TIMES_DEF,
    ): Result<List<GithubRepoDTO>> {
        val result = kotlin.runCatching {
            _api.getGithubRepos(
                page = page,
                perPage = perPage
            ).map {
                it.toGithubRepoDTO()
            }
        }
        if (result.isSuccess) {
            return result
        }

        return when {

            result.isSuccess -> {
                result
            }

            !networkCheck.checkIsNetworkOnline() -> Result.failure(
                CusException(
                    ErrorCode.ERROR_NO_NETWORK,
                    "Network offline"
                )
            )

            retryTimes > 0 -> {
                retryLoadItemForPage(
                    userName,
                    page,
                    perPage,
                    retryTimes - 1
                )
            }

            else -> {
                result
            }
        }
    }

    override suspend fun saveItems(item: List<GithubRepoDTO>) {
        throw IllegalStateException("Not support save to cloud")
    }

    override suspend fun saveUserInfo(userResponse: GithubUserDTO) {
        throw IllegalStateException("Not support save to cloud")
    }

    companion object {
        private const val RETRY_TIMES_DEF = 3
    }


}