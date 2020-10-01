package com.jensencelestial.ghiblibrary.android.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jensencelestial.ghiblibrary.android.data.model.Species
import com.jensencelestial.ghiblibrary.android.data.repository.SpeciesRepository
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import com.jensencelestial.ghiblibrary.android.ui.UIState
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class SpeciesViewModel @ViewModelInject constructor(
    private val speciesRepository: SpeciesRepository
) : ViewModel() {
    private val _species: MutableLiveData<UIState<List<Species>>> = MutableLiveData()

    val species: LiveData<UIState<List<Species>>> = _species

    init {
        getSpecies()
    }

    fun getSpecies() {
        viewModelScope.launch {
            _species.value = UIState.loading()

            when (val getSpeciesResult: RepResult<List<Species>> = speciesRepository.getSpecies()) {
                is RepResult.Success -> {
                    _species.value = UIState.success(getSpeciesResult.result)
                }
                is RepResult.Error<*> -> {
                    when (getSpeciesResult.exception) {
                        is Exception -> {
                            _species.value = UIState.failure()
                        }
                        is SocketTimeoutException -> {
                            _species.value = UIState.failure()
                        }
                    }
                }
            }
        }
    }
}