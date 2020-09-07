package com.jensencelestial.ghiblibrary.android.ui

sealed class UIState<out T> {
    data class Loading<out T>(val value: T? = null) : UIState<T>()

    data class Success<out T>(val value: T) : UIState<T>()

    data class Failure(val message: String? = null) : UIState<Nothing>()

    companion object {
        fun <T> loading(value: T? = null): UIState<T> = Loading(value)

        fun <T> success(value: T): UIState<T> = Success(value)

        fun <T> failure(message: String? = null): UIState<T> = Failure(message)
    }
}