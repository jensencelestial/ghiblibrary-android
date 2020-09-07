package com.jensencelestial.ghiblibrary.android.data.repository.result

/**
 * Class encapsulating the result and errors captured during a Repository Operation.
 */
sealed class RepResult<out T> {
    data class Success<out T>(val result: T) : RepResult<T>()

    data class Error<E : Throwable>(
        val exception: E? = null,
        val message: String? = null
    ) : RepResult<Nothing>()
}