package com.jensencelestial.ghiblibrary.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jensencelestial.ghiblibrary.android.data.model.Person
import com.jensencelestial.ghiblibrary.android.data.repository.PersonRepository
import com.jensencelestial.ghiblibrary.android.data.repository.result.RepResult
import com.jensencelestial.ghiblibrary.android.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {
    private val _people: MutableLiveData<UIState<List<Person>>> = MutableLiveData()

    val people: LiveData<UIState<List<Person>>> = _people

    init {
        getPeople()
    }

    fun getPeople() {
        viewModelScope.launch {
            _people.value = UIState.loading()

            when (val getPeopleResult: RepResult<List<Person>> = personRepository.getPeople()) {
                is RepResult.Success -> {
                    _people.value = UIState.success(getPeopleResult.result)
                }
                is RepResult.Error<*> -> {
                    when (getPeopleResult.exception) {
                        is Exception -> {
                            _people.value = UIState.failure()
                        }
                        is SocketTimeoutException -> {
                            _people.value = UIState.failure()
                        }
                    }
                }
            }
        }
    }
}