package com.ibsystem.temifooddelivery.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.ibsystem.temifooddelivery.utils.Constants.PRODUCT_ARGUMENT_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    // saveStatedHandle: SavedStateHandle
) : ViewModel() {

//    private val _selectedProduct: MutableStateFlow<ProductItem?> = MutableStateFlow(null)
//    val selectedProduct: StateFlow<ProductItem?> = _selectedProduct

    init {
        viewModelScope.launch(Dispatchers.IO) {
//            val productId = saveStatedHandle.get<Int>(PRODUCT_ARGUMENT_KEY) ?: 0
//            _selectedProduct.value = useCases.getSelectedProductUseCase.invoke(productId = productId)
        }
    }


}