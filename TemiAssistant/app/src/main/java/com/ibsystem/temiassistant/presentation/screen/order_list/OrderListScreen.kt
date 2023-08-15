package com.ibsystem.temiassistant.presentation.screen.order_list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.domain.model.OrderModelItem
import com.ibsystem.temiassistant.data.datasource.ApiResult
import com.ibsystem.temiassistant.navigation.Screen
import com.ibsystem.temiassistant.presentation.common.component.GifImage
import com.ibsystem.temiassistant.presentation.common.content.ListOrder
import com.ibsystem.temiassistant.presentation.common.component.TopBarSection

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OrderListScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel,
    navController: NavController
) {
    val uiState = viewModel.uiState.collectAsState()
    val orderList = viewModel.orderList.collectAsState()

    Log.i("OrderListScreen", orderList.toString())

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBarSection(
                username = "Order",
                onBack = { navController.navigate(Screen.MainScreen.route) }
            )
        },
        content = {
            GifImage(
                modifier = Modifier.fillMaxSize(),
                gif = R.drawable.lantern
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                when(uiState.value) {
                    is ApiResult.Error -> Log.i("API","ERROR!")
                    ApiResult.Loading -> CircularProgressIndicator()
                    is ApiResult.Success -> {
                        val orders = orderList.value as? List<OrderModelItem>
                        if (orders != null) {
                            ListOrder(title = "オーダーリスト", orders = orders, viewModel = viewModel, navController = navController)
                        }
                    }
                }
            }
        }
    )
}





