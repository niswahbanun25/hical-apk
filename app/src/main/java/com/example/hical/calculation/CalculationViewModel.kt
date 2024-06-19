package com.example.hical.calculation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.hical.api.Result
import com.example.hical.repository.RetrofitRepository
import com.example.hical.response.AddItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalculationViewModel(private val retrofitrepository: RetrofitRepository) : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _addItemResponse = MutableLiveData<AddItemResponse>()
    val addItemResponse: LiveData<AddItemResponse> = _addItemResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun addMealsItems(meal: String, amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            retrofitrepository.addItems(meal, amount).asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _isLoading.postValue(true)
                    }

                    is Result.Success -> {
                        _isLoading.postValue(false)
                        _addItemResponse.postValue(result.data)
                    }

                    is Result.Error -> {
                        _isLoading.postValue(false)
                        _errorMessage.postValue(result.error)
                    }
                }
            }
        }
    }
}