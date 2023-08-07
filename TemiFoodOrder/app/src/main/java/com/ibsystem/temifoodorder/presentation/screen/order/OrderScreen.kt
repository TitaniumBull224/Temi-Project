package com.ibsystem.temifoodorder.presentation.screen.order

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifoodorder.R
import com.ibsystem.temifoodorder.domain.model.OrderModelItem
import com.ibsystem.temifoodorder.presentation.common.card.CategoryCard
import com.ibsystem.temifoodorder.presentation.component.SearchViewBar
import com.ibsystem.temifoodorder.ui.theme.Black
import com.ibsystem.temifoodorder.ui.theme.DIMENS_10dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_12dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_16dp
import com.ibsystem.temifoodorder.ui.theme.GilroyFontFamily
import com.ibsystem.temifoodorder.ui.theme.TEXT_SIZE_18sp


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OrderScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel
) {
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
                    val orders = orderList.value as? List<OrderModelItem>
                    if (orders != null) {
                        ListOrder(title = "オーダーリスト", orders = orders, viewModel = viewModel)
                    }
                }

                else -> {}
            }
        }
    }
}