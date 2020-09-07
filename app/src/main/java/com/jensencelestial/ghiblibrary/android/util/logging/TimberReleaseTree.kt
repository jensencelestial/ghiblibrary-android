package com.jensencelestial.ghiblibrary.android.util.logging

import timber.log.Timber

class TimberReleaseTree : Timber.Tree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        TODO("Not yet implemented")
    }

}