package com.ibsystem.temifoodorder.presentation.screen.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.ibsystem.temifoodorder.R
import com.ibsystem.temifoodorder.presentation.common.content.ListContentCart
import com.ibsystem.temifoodorder.ui.theme.Black
import com.ibsystem.temifoodorder.ui.theme.DIMENS_16dp
import com.ibsystem.temifoodorder.ui.theme.GilroyFontFamily
import com.ibsystem.temifoodorder.ui.theme.TEXT_SIZE_18sp

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val productCartList by cartViewModel.productCartList.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = DIMENS_16dp),
            text = stringResource(R.string.my_cart),
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = TEXT_SIZE_18sp,
            color = Black
        )

        Spacer(modifier = Modifier.height(DIMENS_16dp))

        ListContentCart(
            cartProducts = productCartList,
            onClickDeleteCart = { productItem ->
                cartViewModel.deleteCart(productItem.copy(isCart = false))
            }
        )
    }
}