package com.ibsystem.temifoodorder.presentation.common.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ibsystem.temifoodorder.domain.model.OrderProduct
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.presentation.common.card.OrderProductCard
import com.ibsystem.temifoodorder.ui.theme.Black
import com.ibsystem.temifoodorder.ui.theme.DIMENS_16dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_1dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_4dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_8dp
import com.ibsystem.temifoodorder.ui.theme.GilroyFontFamily
import com.ibsystem.temifoodorder.ui.theme.GraySecondTextColor
import com.ibsystem.temifoodorder.ui.theme.TEXT_SIZE_18sp

@Composable
fun ListOrderProduct(products: List<ProductItem>, prodQuantity: List<OrderProduct?>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "商品",
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = TEXT_SIZE_18sp,
            color = Black,
            modifier = Modifier.padding(vertical = DIMENS_8dp, horizontal = DIMENS_16dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(DIMENS_4dp)
        ) {
            itemsIndexed(products) { index, product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DIMENS_16dp, vertical = DIMENS_4dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OrderProductCard(product = product, quantity = prodQuantity[index]!!.quantity!!)
                }
                Divider(
                    color = GraySecondTextColor,
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxHeight()
                        .fillMaxWidth()
                )
            }
        }
    }
}