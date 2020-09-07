package com.jensencelestial.ghiblibrary.android.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jensencelestial.ghiblibrary.android.data.model.Film
import com.jensencelestial.ghiblibrary.android.data.repository.FilmRepository
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import com.jensencelestial.ghiblibrary.android.ui.UIState
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class FilmsViewModel @ViewModelInject constructor(
    private val filmRepository: FilmRepository
) : ViewModel() {
    private val _films: MutableLiveData<UIState<List<Film>>> = MutableLiveData()

    val films: LiveData<UIState<List<Film>>> = _films

    init {
        getFilms()
    }

    fun getFilms() {
        viewModelScope.launch {
            _films.value = UIState.loading()

            val getFilmResult: RepResult<List<Film>> = filmRepository.getFilms()

            when (getFilmResult) {
                is RepResult.Success -> {
                    _films.value = UIState.success(getFilmResult.result)
                }
                is RepResult.Error<*> -> {
                    when (getFilmResult.exception) {
                        is Exception -> {
                            _films.value = UIState.failure()
                        }
                        is SocketTimeoutException -> {
                            _films.value = UIState.failure()
                        }
                    }
                }
            }
        }
    }
}