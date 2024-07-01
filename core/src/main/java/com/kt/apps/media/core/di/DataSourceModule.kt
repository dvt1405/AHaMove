package com.kt.apps.media.core.di

import com.kt.apps.media.core.datasource.IGithubDataSource
import com.kt.apps.media.core.datasource.local.LocalGithubDataSourceImpl
import com.kt.apps.media.core.datasource.network.NetworkGithubDataSourceImpl
import com.kt.apps.media.core.di.qualifiers.GithubDataSourceQualifier
import com.kt.apps.media.core.datasource.INetworkCheckDataSource
import com.kt.apps.media.core.datasource.local.NetworkCheckDataSourceImpl
import com.kt.apps.media.core.storage.keyvalue.IKeyValueStorage
import com.kt.apps.media.core.storage.keyvalue.KeyValueStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsKeyValueStorage(instance: KeyValueStorageImpl): IKeyValueStorage

    @Binds
    @Singleton
    abstract fun bindsNetworkCheckRepo(instance: NetworkCheckDataSourceImpl): INetworkCheckDataSource

    @Binds
    @Singleton
    @GithubDataSourceQualifier(GithubDataSourceQualifier.Type.Network)
    abstract fun bindsNetworkGithubDataSource(instance: NetworkGithubDataSourceImpl): IGithubDataSource

    @Binds
    @Singleton
    @GithubDataSourceQualifier(GithubDataSourceQualifier.Type.Local)
    abstract fun bindsLocalGithubDataSource(instance: LocalGithubDataSourceImpl): IGithubDataSource
}