package com.jensencelestial.ghiblibrary.android.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jensencelestial.ghiblibrary.android.data.model.Vehicle
import com.jensencelestial.ghiblibrary.android.data.repository.VehicleRepository
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import com.jensencelestial.ghiblibrary.android.ui.UIState
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class VehiclesViewModel @ViewModelInject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {
    private val _vehicles: MutableLiveData<UIState<List<Vehicle>>> = MutableLiveData()

    val vehicles: LiveData<UIState<List<Vehicle>>> = _vehicles

    init {
        getVehicles()
    }

    fun getVehicles() {
        viewModelScope.launch {
            _vehicles.value = UIState.loading()

            when (val getVehiclesResult: RepResult<List<Vehicle>> =
                vehicleRepository.getVehicles()) {
                is RepResult.Success -> {
                    _vehicles.value = UIState.success(getVehiclesResult.result)
                }
                is RepResult.Error<*> -> {
                    when (getVehiclesResult.exception) {
                        is Exception -> {
                            _vehicles.value = UIState.failure()
                        }
                        is SocketTimeoutException -> {
                            _vehicles.value = UIState.failure()
                        }
                    }
                }
            }
        }
    }
}