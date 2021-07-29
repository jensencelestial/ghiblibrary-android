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
class FilmsViewModel @Inject constructor(
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

            when (val getFilmsResult: RepResult<List<Film>> = filmRepository.getFilms()) {
                is RepResult.Success -> {
                    _films.value = UIState.success(getFilmsResult.result)
                }
                is RepResult.Error<*> -> {
                    when (getFilmsResult.exception) {
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