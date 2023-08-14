package com.ibsystem.temifoodorder.presentation.screen.cart

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ibsystem.temifoodorder.R
import com.ibsystem.temifoodorder.domain.model.OrderItem
import com.ibsystem.temifoodorder.domain.model.OrderProduct
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.navigation.screen.BottomNavItemScreen
import com.ibsystem.temifoodorder.presentation.common.content.ListContentCart
import com.ibsystem.temifoodorder.presentation.screen.home.clickToCart
import com.ibsystem.temifoodorder.presentation.screen.order.OrderViewModel
import com.ibsystem.temifoodorder.ui.theme.Black
import com.ibsystem.temifoodorder.ui.theme.DIMENS_10dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_14dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_16dp
import com.ibsystem.temifoodorder.ui.theme.GilroyFontFamily
import com.ibsystem.temifoodorder.ui.theme.Green
import com.ibsystem.temifoodorder.ui.theme.TEXT_SIZE_18sp
import com.ibsystem.temifoodorder.utils.showToastShort

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    orderViewModel: OrderViewModel = hiltViewModel()
) {
    val mContext = LocalContext.current

    val productCartList by orderViewModel.productCartList.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(Modifier.weight(1f)) {
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
                // onClickDeleteCart = { productItem ->
                //     cartViewModel.deleteCart(productItem.copy(id = "1"))
                // }
            )
        }

        if(productCartList.isNotEmpty()) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = DIMENS_16dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                shape = RoundedCornerShape(DIMENS_14dp),
                contentPadding = PaddingValues(DIMENS_10dp),
                onClick = {
                    clickToOrder(context = mContext, productCartList = productCartList, orderViewModel = orderViewModel)
                }
            ) {
                Text(text = "注文", color = Color.White,)
            }
        }


    }



}




fun clickToOrder(context: Context, productCartList: Map<ProductItem, OrderProduct>, orderViewModel: OrderViewModel) {
    context.showToastShort("ご注文ありがとうございます！")
    orderViewModel.insertNewOrder(productCartList = productCartList)
}

@Preview
@Composable
fun preview() {
    CartScreen()
}
