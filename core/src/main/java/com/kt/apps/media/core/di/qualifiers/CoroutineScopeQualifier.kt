package com.kt.apps.media.core.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CoroutineScopeQualifier(
    val value: CoroutineDispatcherType
) {
}

enum class CoroutineDispatcherType {
    IO, DEFAULT, MAIN
}