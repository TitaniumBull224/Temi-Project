package com.ibsystem.temifoodorder.presentation.common.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.ui.theme.Black
import com.ibsystem.temifoodorder.ui.theme.DIMENS_12dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_174dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_20dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_24dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_80dp
import com.ibsystem.temifoodorder.ui.theme.GilroyFontFamily
import com.ibsystem.temifoodorder.ui.theme.GrayBorderStroke
import com.ibsystem.temifoodorder.ui.theme.TEXT_SIZE_16sp
import com.ibsystem.temifoodorder.ui.theme.TEXT_SIZE_18sp
import com.ibsystem.temifoodorder.ui.theme.TEXT_SIZE_24sp

@Composable
fun OrderProductCard(
    modifier: Modifier = Modifier,
    product: ProductItem,
    quantity: Int
) {
    Card(
        shape = RoundedCornerShape(DIMENS_12dp),
        border = BorderStroke(width = 1.dp, color = GrayBorderStroke),
        modifier = modifier
            .padding(DIMENS_12dp)
            .width(DIMENS_174dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DIMENS_12dp)
        ) {
            Image(
                painter = rememberImagePainter(product.prodImage),
                contentDescription = "ProductCard",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .height(DIMENS_80dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(DIMENS_24dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = product.prodName!!,
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        fontSize = TEXT_SIZE_16sp
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text =  "x$quantity",
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        fontSize = TEXT_SIZE_24sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(DIMENS_20dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¥" + product.prodPrice.toString() ,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = TEXT_SIZE_18sp
                )
            }

        }
    }
}