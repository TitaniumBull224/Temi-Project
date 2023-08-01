package com.ibsystem.temifooddelivery.presentation.screen.order_list

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import com.ibsystem.temifooddelivery.presentation.common.content.ListOrder
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OrderListScreen(modifier: Modifier = Modifier,
viewModel: OrderViewModel
) {
    val uiState = viewModel.uiState.collectAsState()
    val orderList = viewModel.orderList.collectAsState()

    Log.i("SIGH", orderList.toString())

    Scaffold(modifier = modifier.fillMaxSize(), content = {
        when(uiState.value) {
            is ApiResult.Error -> TODO()
            ApiResult.Loading -> CircularProgressIndicator()
            is ApiResult.Success -> {
                val orders = orderList.value as? List<OrderModelItem>
//                    LazyColumn {
//                        items(orders ?: listOf()) { order ->
//                            Text(text = order.toString(),modifier = Modifier
//                                .padding(10.dp)
//                                .fillMaxWidth())
//                        }
//                    }
                if (orders != null) {
                    ListOrder(title = "オーダーリスト", orders = orders)
                }
            }
        }
    })


}

