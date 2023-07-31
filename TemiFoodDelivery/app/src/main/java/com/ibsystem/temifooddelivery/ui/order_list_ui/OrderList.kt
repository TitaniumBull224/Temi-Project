package com.ibsystem.temifooddelivery.ui.order_list_ui

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ibsystem.temifooddelivery.OrderViewModel
import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifooddelivery.domain.OrderModelItem

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderListScreen(modifier: Modifier = Modifier,
viewModel: OrderViewModel) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(modifier = modifier.fillMaxSize(), content = {
        when(uiState.value) {
            is ApiResult.Error -> TODO()
            ApiResult.Loading -> CircularProgressIndicator()
            is ApiResult.Success -> {
                val orders = (uiState.value as ApiResult.Success<*>).data as? List<OrderModelItem>
                LazyColumn {
                    items(orders ?: listOf()) { order ->
                        Text(text = order.toString(),modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth())
                    }
                }
            }
        }

    })
}

