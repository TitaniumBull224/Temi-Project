package com.ibsystem.temifooddelivery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifooddelivery.data.repository.OrderRepository
import com.ibsystem.temifooddelivery.data.repository.OrderRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor (
    private val repository: OrderRepository): ViewModel() {

        private val _uiState = MutableStateFlow<ApiResult<*>>(ApiResult.Loading)
        val uiState: StateFlow<ApiResult<*>>
            get() = _uiState

    fun getAllOrders() {
        viewModelScope.launch {
            repository.getAllOrders().collectLatest {
                data -> _uiState.update { data }
            }
        }
    }
}