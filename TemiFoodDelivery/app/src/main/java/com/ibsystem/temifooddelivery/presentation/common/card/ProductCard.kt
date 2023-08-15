package com.ibsystem.temifooddelivery.presentation.common.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import com.ibsystem.temifooddelivery.ui.theme.*
import androidx.compose.runtime.*

import androidx.compose.foundation.lazy.items
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import com.ibsystem.temifooddelivery.domain.Product


@Composable
fun ExpandableCard(orderItem: OrderModelItem) {
    var expanded by remember { mutableStateOf (false) }

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(
                    onClick = { expanded = !expanded }
                )
        ) {
            Column() {
                Text(
                    text = orderItem.tableId!!,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(8.dp)
                )
                if (expanded) {
                    LazyRow(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    ) {
                        items(orderItem.product!!) {product ->
                            Text(
                                text = product.prodName!!,
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                    }

                }
            }
        }
}

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    quantity: Int = 1
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
                    text = product.prodPrice.toString() + "¥",
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

@Preview
@Composable
fun ProductPreview() {
    ProductCard(
        product = Product(
            catId = "aishhvaisuhg",
            prodAvail = true,
            prodDesc = "god damn",
            prodId = "foauhvauhuahg",
            prodImage = "https://burpple-2.imgix.net/foods/18701ea9eb80bcd299c1559365_original.",
            prodPrice = 598,
            prodName = "焼肉"
        ),
        quantity = 3
    )
}