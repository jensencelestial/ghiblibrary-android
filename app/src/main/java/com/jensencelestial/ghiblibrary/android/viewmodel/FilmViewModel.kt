package com.jensencelestial.ghiblibrary.android.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jensencelestial.ghiblibrary.android.data.repository.FilmRepository
import kotlinx.coroutines.launch

class FilmViewModel @ViewModelInject constructor(
    private val filmRepository: FilmRepository
) : ViewModel(), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {

    }

    fun getFilm(filmId: Int) {
        viewModelScope.launch {

        }
    }
}