package com.ibsystem.temifoodorder.presentation.screen.order

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ibsystem.temifoodorder.domain.model.OrderItem
import com.ibsystem.temifoodorder.domain.model.OrderDetailItem
import com.ibsystem.temifoodorder.utils.ApiResult


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = hiltViewModel()
) {
//    viewModel.getAllOrders()
//    viewModel.listenToOrdersChange()
    Log.e("OrderSCRN", viewModel.hashCode().toString())
    val uiState = viewModel.uiState.collectAsState()
    val orderList = viewModel.orderList.collectAsState()
    Scaffold(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when(uiState.value) {
                is ApiResult.Error -> {
                    Log.i("ERRR", "ERROR")
                }
                ApiResult.Loading -> CircularProgressIndicator()
                is ApiResult.Success -> {
                    val orders = orderList.value as? List<OrderDetailItem>
                    if (orders != null) {
                        ListOrder(title = "オーダーリスト", orders = orders)
                    }
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Button(onClick = {viewModel.insertNewOrder()}) {
//                Text(text = "TEST")
//            }
        }
    }
}