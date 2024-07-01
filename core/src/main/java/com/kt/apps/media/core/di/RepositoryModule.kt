package com.kt.apps.media.core.di

import com.kt.apps.media.core.repository.GithubRepositoryImpl
import com.kt.apps.media.core.repository.IGithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsRepository(instance: GithubRepositoryImpl): IGithubRepository
}