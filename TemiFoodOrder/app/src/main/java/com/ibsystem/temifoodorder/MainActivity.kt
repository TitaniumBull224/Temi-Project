package com.ibsystem.temifoodorder

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.ibsystem.temifoodorder.datasource.ApiResult
import com.ibsystem.temifoodorder.domain.model.CategoryItem
import dagger.hilt.android.AndroidEntryPoint
import com.ibsystem.temifoodorder.navigation.graph.RootNavigationGraph
import com.ibsystem.temifoodorder.presentation.screen.home.HomeViewModel
import com.ibsystem.temifoodorder.ui.theme.GroceriesAppTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.findAllCategories()
        setContent {
            GroceriesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RootNavigationGraph(navController = rememberNavController())
                }
            }
        }


    }
    private fun observeData() {
        lifecycleScope.launch {
            homeViewModel.uiState.collectLatest { data ->
                when(data) {
                    is ApiResult.Error -> {
                        Log.e("MainActivity", "Message ${data.message}")
                    }
                    is ApiResult.Loading -> {
                        Log.e("MainActivity", "loading")
                    }
                    is ApiResult.Success -> {
                        val categories = data.data as List<CategoryItem>
                        categories.forEach {
                            Log.e("MainActivity", it.toString())
                        }
                    }
                }

            }
        }
    }
}