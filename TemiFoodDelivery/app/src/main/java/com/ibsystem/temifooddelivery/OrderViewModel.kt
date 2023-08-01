package com.ibsystem.temifooddelivery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifooddelivery.data.repository.OrderRepository
import com.ibsystem.temifooddelivery.data.repository.OrderRepositoryImpl
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.realtime.PostgresAction
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

        private val _orderList = MutableStateFlow<List<OrderModelItem>>(mutableListOf())
        val orderList: StateFlow<List<OrderModelItem>>
            get() = _orderList



    fun getAllOrders() {
        viewModelScope.launch {
            repository.getAllOrders().collectLatest {
                data -> _uiState.update { data }
                 if(data is ApiResult.Success) {
                     val orderList = data.data
                     _orderList.value = orderList
                 }
            }
        }
    }

    fun listenToOrdersChange() {
        viewModelScope.launch {
            repository.listenToOrdersChange().collect {
            when (it) {
                    is PostgresAction.Delete -> Log.i("Listener","Deleted: ${it.oldRecord}")
                    is PostgresAction.Insert -> Log.i("Listener","Inserted: ${it.record}")
                    is PostgresAction.Select -> Log.i("Listener","Selected: ${it.record}")
                    is PostgresAction.Update -> Log.i("Listener","Updated: ${it.oldRecord} with ${it.record}")
                else -> Log.i("Listener","sighhh")
                }
            }
        }
    }
}