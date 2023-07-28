package com.ibsystem.temifoodorder.presentation.screen.home

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ibsystem.temifoodorder.R
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.navigation.screen.Screen
import com.ibsystem.temifoodorder.presentation.common.content.ListContentProduct
import com.ibsystem.temifoodorder.presentation.component.SearchViewBar
import com.ibsystem.temifoodorder.presentation.component.SliderBanner
import com.ibsystem.temifoodorder.ui.theme.*
import com.ibsystem.temifoodorder.utils.showToastShort

@ExperimentalPagerApi
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
//    val mContext = LocalContext.current
//    val searchQuery by homeViewModel.searchQuery
//    val allProducts by homeViewModel.productList.collectAsState()
//
//    Scaffold { padding ->
//        Column(
//            modifier = modifier
//                .verticalScroll(rememberScrollState())
//                .padding(padding)
//        ) {
//            HeaderLocationHome()
//
//            SearchViewBar(
//                hint = stringResource(id = R.string.search_store),
//                query = searchQuery,
//                onValueChange = {
//                    if (it.isNotEmpty()) navController.navigate(Screen.Search.route)
//                }
//            )
//
//            SliderBanner()
//
//            ListContentProduct(
//                title = stringResource(id = R.string.exclusive_offer),
//                products = allProducts,
//                navController = navController,
//                onClickToCart = { productItem ->
//                    clickToCart(mContext, productItem, homeViewModel)
//                }
//            )
//
//            Spacer(modifier = Modifier.height(DIMENS_24dp))
//
//            ListContentProduct(
//                title = stringResource(id = R.string.best_selling),
//                products = allProducts.sortedByDescending { it.id },
//                navController = navController,
//                onClickToCart = { productItem ->
//                    clickToCart(mContext, productItem, homeViewModel)
//                }
//            )
//        }
//    }
}

//@Composable
//fun HeaderLocationHome(
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(DIMENS_24dp))
//
//        Icon(
//            modifier = Modifier
//                .size(DIMENS_24dp)
//                .align(Alignment.CenterHorizontally),
//            painter = painterResource(id = R.drawable.ic_nectar),
//            contentDescription = stringResource(id = R.string.logo_app),
//            tint = Color.Unspecified
//        )
//
//        Spacer(modifier = Modifier.height(DIMENS_8dp))
//
//        Row {
//            Icon(
//                imageVector = Icons.Default.LocationOn,
//                contentDescription = stringResource(R.string.image_location),
//                tint = GrayThirdTextColor
//            )
//            Text(
//                modifier = Modifier.align(Alignment.CenterVertically),
//                text = stringResource(R.string.sample_country),
//                fontFamily = GilroyFontFamily,
//                fontWeight = FontWeight.SemiBold,
//                fontSize = TEXT_SIZE_12sp,
//                color = GrayThirdTextColor
//            )
//        }
//    }
//}
//
//fun clickToCart(context: Context, productItem: ProductItem, viewModel: HomeViewModel) {
//    context.showToastShort("Success Add To Cart ${productItem.title}")
//    viewModel.addCart(productItem.copy(isCart = true))
//}
//
//@Preview(showBackground = true)
//@Composable
//fun HeaderLocationHomePreview() {
//    HeaderLocationHome()
//}