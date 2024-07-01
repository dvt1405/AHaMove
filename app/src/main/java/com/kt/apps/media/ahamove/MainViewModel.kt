package com.kt.apps.media.ahamove

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kt.apps.media.core.Constants
import com.kt.apps.media.core.datasource.INetworkCheckDataSource
import com.kt.apps.media.core.exceptions.CusException
import com.kt.apps.media.core.models.DataState
import com.kt.apps.media.core.models.GithubRepoDTO
import com.kt.apps.media.core.models.GithubUserDTO
import com.kt.apps.media.core.repository.IGithubRepository
import com.kt.apps.media.core.storage.database.dbmodels.GithubRepo
import com.kt.apps.media.core.storage.keyvalue.IKeyValueStorage
import com.kt.apps.media.core.utils.ErrorCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IGithubRepository,
    private val keyValueStorage: IKeyValueStorage,
    private val networkDataSource: INetworkCheckDataSource
) : ViewModel() {

    private var currentPage = 0

    private val _githubUserInfo by lazy {
        MutableLiveData<DataState<GithubUserDTO>>(DataState.Loading())
    }

    private val _githubRepos by lazy {
        MutableLiveData<DataState<List<GithubRepoDTO>>>(DataState.Loading())
    }

    private val _networkState by lazy {
        MutableLiveData<Boolean>(null)
    }

    val githubUserInfo: LiveData<DataState<GithubUserDTO>>
        get() = _githubUserInfo

    val githubRepos: LiveData<DataState<List<GithubRepoDTO>>>
        get() = _githubRepos

    val networkState: LiveData<Boolean>
        get() = _networkState

    init {
        getGithubUser()
        loadRepos()
        collectNetworkState()
    }

    private fun collectNetworkState() {
        viewModelScope.launch(Dispatchers.IO) {
            networkDataSource.networkState()
                .collect {
                    if (_networkState.value != it) {
                        _networkState.postValue(it)
                    }
                }
        }
    }

    private fun getGithubUser() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_githubUserInfo.value !is DataState.Loading) {
                _githubUserInfo.postValue(DataState.Loading())
            }
            try {
                _githubUserInfo.postValue(DataState.Success(repository.getInfo(Constants.GITHUB_USER_NAME)))
                repository.updateUserInfoIfNeeded(Constants.GITHUB_USER_NAME)
            } catch (e: CusException) {
                when (e.errorCode) {
                    ErrorCode.ERROR_NO_NETWORK -> {
                        _githubUserInfo.postValue(DataState.Error(Throwable("No network connection, please try again later!")))
                    }

                    ErrorCode.ERROR_NETWORK_TIMEOUT -> {
                        _githubUserInfo.postValue(DataState.Error(Throwable("Connection is unstable, please try again later!")))
                    }

                    ErrorCode.UNKNOWN_ERROR -> {
                        _githubUserInfo.postValue(DataState.Error(Throwable("Unknown error")))
                    }
                }
            } catch (e: Exception) {
                _githubUserInfo.postValue(DataState.Error(Throwable("Unknown Error")))
            }
        }
    }

    fun loadRepos() {
        if (!repository.canLoadMore()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val listItem = repository.loadItemForPage(
                    Constants.GITHUB_USER_NAME,
                    currentPage,
                    20
                )
                currentPage++
                _githubRepos.postValue(DataState.Success(listItem))
            } catch (e: Exception) {
            }
        }
    }

    fun refreshRepos() {
        currentPage = 0
        loadRepos()
        if (_githubUserInfo.value is DataState.Error) {
            getGithubUser()
        }
    }

}