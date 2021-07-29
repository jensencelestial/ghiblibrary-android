package com.jensencelestial.ghiblibrary.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jensencelestial.ghiblibrary.android.data.model.Film
import com.jensencelestial.ghiblibrary.android.data.repository.FilmRepository
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import com.jensencelestial.ghiblibrary.android.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(
    private val filmRepository: FilmRepository
) : ViewModel() {

    private val _film: MutableLiveData<UIState<Film>> = MutableLiveData()

    val film: LiveData<UIState<Film>> = _film

    fun getFilm(filmId: String) {
        viewModelScope.launch {
            _film.value = UIState.loading()

            when (val getFilmResult: RepResult<Film> = filmRepository.getFilm(filmId)) {
                is RepResult.Success -> {
                    _film.value = UIState.success(getFilmResult.result)
                }
                is RepResult.Error<*> -> {
                    when (getFilmResult.exception) {
                        is Exception -> {
                            _film.value = UIState.failure()
                        }
                        is SocketTimeoutException -> {
                            _film.value = UIState.failure()
                        }
                    }
                }
            }
        }
    }
}