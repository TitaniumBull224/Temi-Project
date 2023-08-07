package com.ibsystem.temifoodorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.ibsystem.temifoodorder.navigation.graph.RootNavigationGraph
import com.ibsystem.temifoodorder.presentation.screen.order.OrderViewModel
import com.ibsystem.temifoodorder.ui.theme.GroceriesAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GroceriesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RootNavigationGraph(navController = rememberNavController(), orderViewModel)
                }
            }
        }
    }

}