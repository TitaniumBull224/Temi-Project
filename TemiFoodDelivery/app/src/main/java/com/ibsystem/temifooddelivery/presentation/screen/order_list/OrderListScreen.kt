package com.ibsystem.temifooddelivery.presentation.screen.order_list

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import com.ibsystem.temifooddelivery.presentation.common.content.ListOrder
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OrderListScreen(modifier: Modifier = Modifier,
viewModel: OrderViewModel, navController: NavController
) {
    val uiState = viewModel.uiState.collectAsState()
    val orderList = viewModel.orderList.collectAsState()

    Log.i("SIGH", orderList.toString())

    Scaffold(modifier = modifier.fillMaxSize(), content = {Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(uiState.value) {
            is ApiResult.Error -> Log.i("ERRR","YOUFJFKFLGGDFKGFDKGDKGDFKGDFGKFGDK")
            ApiResult.Loading -> CircularProgressIndicator()
            is ApiResult.Success -> {
                val orders = orderList.value as? List<OrderModelItem>
                if (orders != null) {
                    ListOrder(title = "オーダーリスト", orders = orders, viewModel = viewModel, navController = navController)
                }
            }
        }
    }
    })


}

