package com.kt.apps.media.core.di

import android.content.Context
import com.kt.apps.media.core.storage.database.MyDatabase
import com.kt.apps.media.core.storage.database.dao.GithubReposDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    fun provideDataBase(
        @ApplicationContext
        context: Context
    ): MyDatabase {
        return MyDatabase.getInstance(context = context)
    }

    @Provides
    fun provideGithubRepoDao(myDatabase: MyDatabase): GithubReposDao {
        return myDatabase.githubReposDao()
    }
}