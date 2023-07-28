package com.ibsystem.temifoodorder.presentation.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibsystem.temifoodorder.datasource.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.repository.SupabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SupabaseRepository
) : ViewModel() {

//    private val _searchQuery = mutableStateOf("")
//    val searchQuery = _searchQuery
//
//    private val _productList = MutableStateFlow<List<ProductItem>>(emptyList())
//    val productList = _productList.asStateFlow()
//
//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            useCases.getAllProductUseCase.invoke().collect { value ->
//                _productList.value = value
//            }
//        }
//    }
//
//    fun addCart(productItem: ProductItem) = viewModelScope.launch {
//        useCases.addCartUseCase.invoke(productItem)
//    }

    private val _uiState = MutableStateFlow<ApiResult<*>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<*>>
        get() = _uiState

    fun findAllCategories() {
        viewModelScope.launch {
            repository.findAllCategories().collectLatest {
                data-> _uiState.update { data }
            }
        }
    }
}