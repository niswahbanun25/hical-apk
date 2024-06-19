package com.example.hical.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.hical.api.Result
import com.example.hical.repository.RetrofitRepository
import com.example.hical.response.DashboardItem
import com.example.hical.response.DeletePayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardViewModel(private val retrofitrepository: RetrofitRepository) : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _dashboardItems = MutableLiveData<ArrayList<DashboardItem>?>(arrayListOf())
    val dashboardItem: LiveData<ArrayList<DashboardItem>?> = _dashboardItems

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        fetchDashboardItems()
    }

    fun fetchDashboardItems() {
        viewModelScope.launch(Dispatchers.IO) {
            retrofitrepository.fetchItems().asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _isLoading.postValue(true)
                    }

                    is Result.Success -> {
                        _isLoading.postValue(false)
                        _dashboardItems.postValue(result.data.item as ArrayList<DashboardItem>?)
                    }

                    is Result.Error -> {
                        _isLoading.postValue(false)
                        _errorMessage.postValue(result.error)
                    }
                }
            }
        }
    }

    fun deleteItem(deletePayload: DeletePayload) = retrofitrepository.deleteItem(deletePayload)
}