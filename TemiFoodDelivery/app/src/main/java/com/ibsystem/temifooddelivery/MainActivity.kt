package com.ibsystem.temifooddelivery

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.ibsystem.temifooddelivery.data.datasource.ApiResult
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import com.ibsystem.temifooddelivery.domain.OrderProduct
import com.ibsystem.temifooddelivery.domain.Product
import com.ibsystem.temifooddelivery.presentation.screen.customer.CustomerScreen
import com.ibsystem.temifooddelivery.presentation.screen.order_list.OrderListScreen
import com.ibsystem.temifooddelivery.presentation.screen.order_list.OrderViewModel
import com.ibsystem.temifooddelivery.ui.theme.GrayBackground
import com.ibsystem.temifooddelivery.ui.theme.TemiFoodDeliveryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val orderViewModel by viewModels<OrderViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //orderViewModel.getAllOrders()
        //observeData()
        //orderViewModel.listenToOrdersChange()
        setContent {
            TemiFoodDeliveryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GrayBackground
                ) {
                    //OrderListScreen(viewModel = orderViewModel)
                    CustomerScreen(
                        order = OrderModelItem(
                            orderProduct = listOf(OrderProduct(3)),
                            product = listOf(Product(catId = "ds", prodName = "カルビ", prodAvail = true, prodDesc = "edd", prodId = "fdf", prodImage = "redf", prodPrice = 23)),
                            id="2",
                            status = "DS",
                            tableId = "3",
                            time = "2023-08-02T09:53:13.405539+09:00"
                        ), viewModel = orderViewModel
                    )
                }
            }
        }
    }

//    private fun observeData() {
//        lifecycleScope.launch {
//            orderViewModel.uiState.collect {
//                    data -> when(data){
//                is ApiResult.Error -> {
//                    Log.e("Main Act", "Error: ${data.message}")
//                }
//                ApiResult.Loading -> {
//                    Log.i("Main Act", "IS LOADING")
//                }
//                is ApiResult.Success -> {
//                    val orders = data.data as List<*>
//                    orders.forEach{
//                        Log.i("Main Act", it.toString())
//                    }
//                }
//            }
//            }
//        }
//    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TemiFoodDeliveryTheme {
        Greeting("Android")
    }
}