package com.ibsystem.temifoodorder.presentation.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.domain.usecase.UseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    private val _productList = MutableStateFlow<List<ProductItem>>(emptyList())
    val productList = _productList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getAllProductUseCase.invoke().collect { value ->
                _productList.value = value
            }
        }
    }

    fun addCart(productItem: ProductItem) = viewModelScope.launch {
        useCases.addCartUseCase.invoke(productItem)
    }

}