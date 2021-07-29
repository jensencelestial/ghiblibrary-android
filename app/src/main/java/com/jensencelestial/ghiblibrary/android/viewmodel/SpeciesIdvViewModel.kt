package com.jensencelestial.ghiblibrary.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jensencelestial.ghiblibrary.android.data.model.Species
import com.jensencelestial.ghiblibrary.android.data.repository.SpeciesRepository
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import com.jensencelestial.ghiblibrary.android.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SpeciesIdvViewModel @Inject constructor(
    private val speciesRepository: SpeciesRepository
) : ViewModel() {

    private val _species: MutableLiveData<UIState<Species>> = MutableLiveData()

    val species: LiveData<UIState<Species>> = _species

    fun getSpecies(speciesId: String) {
        viewModelScope.launch {
            _species.value = UIState.loading()

            when (val getSpeciesResult: RepResult<Species> =
                speciesRepository.getSpecies(speciesId)) {
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