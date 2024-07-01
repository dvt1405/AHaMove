package com.kt.apps.media.core.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GithubDataSourceQualifier(
    val value: Type
) {
    enum class Type {
        Network, Local
    }
}