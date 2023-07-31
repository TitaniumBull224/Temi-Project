package com.ibsystem.temifooddelivery.presentation.screen.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

//    private val _productList = MutableStateFlow<List<ProductItem>>(emptyList())
//    val productList = _productList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
//            useCases.getAllProductUseCase.invoke().collect { value ->
//                _productList.value = value
//            }
        }
    }



}