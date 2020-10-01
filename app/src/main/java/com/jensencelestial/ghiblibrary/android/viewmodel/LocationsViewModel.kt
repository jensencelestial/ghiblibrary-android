package com.jensencelestial.ghiblibrary.android.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jensencelestial.ghiblibrary.android.data.model.Location
import com.jensencelestial.ghiblibrary.android.data.repository.LocationRepository
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import com.jensencelestial.ghiblibrary.android.ui.UIState
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class LocationsViewModel @ViewModelInject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _locations: MutableLiveData<UIState<List<Location>>> = MutableLiveData()

    val locations: LiveData<UIState<List<Location>>> = _locations

    init {
        getLocations()
    }

    fun getLocations() {
        viewModelScope.launch {
            _locations.value = UIState.loading()

            when (val getLocationsResult: RepResult<List<Location>> =
                locationRepository.getLocations()) {
                is RepResult.Success -> {
                    _locations.value = UIState.success(getLocationsResult.result)
                }
                is RepResult.Error<*> -> {
                    when (getLocationsResult.exception) {
                        is Exception -> {
                            _locations.value = UIState.failure()
                        }
                        is SocketTimeoutException -> {
                            _locations.value = UIState.failure()
                        }
                    }
                }
            }
        }
    }
}