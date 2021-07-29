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
class PersonViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {

    private val _person: MutableLiveData<UIState<Person>> = MutableLiveData()

    val person: LiveData<UIState<Person>> = _person

    fun getPerson(personId: String) {
        viewModelScope.launch {
            _person.value = UIState.loading()

            when (val getPersonResult: RepResult<Person> = personRepository.getPerson(personId)) {
                is RepResult.Success -> {
                    _person.value = UIState.success(getPersonResult.result)
                }
                is RepResult.Error<*> -> {
                    when (getPersonResult.exception) {
                        is Exception -> {
                            _person.value = UIState.failure()
                        }
                        is SocketTimeoutException -> {
                            _person.value = UIState.failure()
                        }
                    }
                }
            }
        }
    }
}