package com.ibsystem.temifooddelivery.presentation.common.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ibsystem.temifooddelivery.R
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import com.ibsystem.temifooddelivery.navigation.screen.Screen
import com.ibsystem.temifooddelivery.ui.theme.*
import androidx.compose.runtime.*

import androidx.compose.foundation.lazy.items



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

//@Composable
//fun ProductCard(
//    modifier: Modifier = Modifier,
//    orderItem: OrderModelItem,
//    navController: NavController,
////    onClickToCart: (ProductItem) -> Unit
//) {
//    Card(
//        shape = RoundedCornerShape(DIMENS_12dp),
//        border = BorderStroke(width = 1.dp, color = GrayBorderStroke),
//        modifier = modifier
//            .padding(DIMENS_12dp)
//            .width(DIMENS_174dp)
//            .clickable {
//                navController.navigate(Screen.Details.passProductId(productId = "1"))
//            }
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(DIMENS_12dp)
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.product1),
//                contentDescription = stringResource(R.string.image_product),
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .fillMaxWidth()
//                    .height(DIMENS_80dp)
//            )
//
//            Spacer(modifier = Modifier.height(DIMENS_24dp))
//
//            Text(
//                text = "product1",
//                fontFamily = GilroyFontFamily,
//                fontWeight = FontWeight.Bold,
//                color = Black,
//                fontSize = TEXT_SIZE_16sp
//            )
//
//            Spacer(modifier = Modifier.height(DIMENS_20dp))
//
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = "3.33",
//                    fontFamily = GilroyFontFamily,
//                    fontWeight = FontWeight.Bold,
//                    color = Black,
//                    modifier = Modifier.align(Alignment.CenterVertically),
//                    fontSize = TEXT_SIZE_18sp
//                )
//
//                Button(
//                    modifier = Modifier.size(DIMENS_46dp),
//                    colors = ButtonDefaults.buttonColors(backgroundColor = Green),
//                    shape = RoundedCornerShape(DIMENS_14dp),
//                    contentPadding = PaddingValues(DIMENS_10dp),
//                    onClick = {
////                        onClickToCart.invoke(productItem)
//                    }
//                )
//                {
//                    Icon(
//                        modifier = Modifier.fillMaxSize(),
//                        imageVector = Icons.Default.Add,
//                        tint = Color.White,
//                        contentDescription = stringResource(id = R.string.add)
//                    )
//                }
//            }
//
//        }
//    }
//}

//@Preview
//@Composable
//fun ItemProductPreview() {
//    ProductCard(
////        productItem = ProductItem(
////            id = "1",
////            name = "Organic Bananas",
////            description = "",
////            image = R.drawable.product10,
////            price = 4.99
////        ),
//        navController = rememberNavController(),
////        onClickToCart = {}
//    )
//}