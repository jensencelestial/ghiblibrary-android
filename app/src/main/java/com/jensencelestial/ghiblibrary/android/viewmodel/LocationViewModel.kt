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

class LocationViewModel @ViewModelInject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _location: MutableLiveData<UIState<Location>> = MutableLiveData()

    val location: LiveData<UIState<Location>> = _location

    fun getLocation(locationId: String) {
        viewModelScope.launch {
            _location.value = UIState.loading()

            when (val getLocationResult: RepResult<Location> =
                locationRepository.getLocation(locationId)) {
                is RepResult.Success -> {
                    _location.value = UIState.success(getLocationResult.result)
                }
                is RepResult.Error<*> -> {
                    when (getLocationResult.exception) {
                        is Exception -> {
                            _location.value = UIState.failure()
                        }
                        is SocketTimeoutException -> {
                            _location.value = UIState.failure()
                        }
                    }
                }
            }
        }
    }
}