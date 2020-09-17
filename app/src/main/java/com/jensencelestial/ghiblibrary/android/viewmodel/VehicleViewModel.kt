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

class VehicleViewModel @ViewModelInject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _vehicle: MutableLiveData<UIState<Vehicle>> = MutableLiveData()

    val vehicle: LiveData<UIState<Vehicle>> = _vehicle

    fun getVehicle(vehicleId: String) {
        viewModelScope.launch {
            _vehicle.value = UIState.loading()

            when (val getVehicleResult: RepResult<Vehicle> =
                vehicleRepository.getVehicle(vehicleId)) {
                is RepResult.Success -> {
                    _vehicle.value = UIState.success(getVehicleResult.result)
                }
                is RepResult.Error<*> -> {
                    when (getVehicleResult.exception) {
                        is Exception -> {
                            _vehicle.value = UIState.failure()
                        }
                        is SocketTimeoutException -> {
                            _vehicle.value = UIState.failure()
                        }
                    }
                }
            }
        }
    }
}