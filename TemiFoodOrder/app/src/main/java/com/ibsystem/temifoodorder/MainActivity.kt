package com.ibsystem.temifoodorder

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.ibsystem.temifoodorder.presentation.screen.MainScreen
import com.ibsystem.temifoodorder.ui.theme.GroceriesAppTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val orderViewModel by viewModels<OrderViewModel>()
//    private val cartViewModel by viewModels<CartViewModel>()
//    private val homeViewModel by viewModels<HomeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GroceriesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //RootNavigationGraph(navController = rememberNavController())
                    MainScreen()
                }
            }

        }
    }

}




