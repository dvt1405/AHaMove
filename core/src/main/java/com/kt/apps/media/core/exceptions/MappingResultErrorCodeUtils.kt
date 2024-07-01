package com.kt.apps.media.core.exceptions

import com.kt.apps.media.core.utils.ErrorCode
import kotlinx.coroutines.TimeoutCancellationException
import java.util.concurrent.TimeoutException

fun <T> Result<T>.mappingErrorCode(): CusException {
    val throwableMapping = when (val ex = this.exceptionOrNull()) {
        is TimeoutException,
        is TimeoutCancellationException -> CusException(
            ErrorCode.ERROR_NETWORK_TIMEOUT,
            ex.message
        )

        else -> CusException(
            ErrorCode.UNKNOWN_ERROR,
            ex?.message
        )
    }
    return throwableMapping
}