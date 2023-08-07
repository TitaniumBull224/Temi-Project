package com.ibsystem.temifooddelivery.presentation.screen.customer


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ibsystem.temifooddelivery.navigation.screen.Screen
import com.ibsystem.temifooddelivery.presentation.common.content.TableCell
import com.ibsystem.temifooddelivery.presentation.common.content.column1Weight
import com.ibsystem.temifooddelivery.presentation.common.content.column2Weight
import com.ibsystem.temifooddelivery.presentation.common.content.column3Weight
import com.ibsystem.temifooddelivery.presentation.common.content.column4Weight
import com.ibsystem.temifooddelivery.presentation.common.content.column5Weight
import com.ibsystem.temifooddelivery.presentation.screen.order_list.OrderViewModel
import com.ibsystem.temifooddelivery.ui.theme.Black
import com.ibsystem.temifooddelivery.ui.theme.DIMENS_16dp
import com.ibsystem.temifooddelivery.ui.theme.DIMENS_2dp
import com.ibsystem.temifooddelivery.ui.theme.DIMENS_4dp
import com.ibsystem.temifooddelivery.ui.theme.DIMENS_8dp
import com.ibsystem.temifooddelivery.ui.theme.GilroyFontFamily
import com.ibsystem.temifooddelivery.ui.theme.GraySecondTextColor
import com.ibsystem.temifooddelivery.ui.theme.TEXT_SIZE_16sp
import com.ibsystem.temifooddelivery.ui.theme.TEXT_SIZE_18sp
import com.ibsystem.temifooddelivery.ui.theme.TEXT_SIZE_24sp
import com.ibsystem.temifooddelivery.ui.theme.White
import com.ibsystem.temifooddelivery.utils.reformatDate

@Composable
fun CustomerScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    orderID: String,
    viewModel: OrderViewModel
) {
    val order = viewModel.findtOrderByID(orderID)
    order?.let {
        val formattedDate = reformatDate(order.time!!)
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement  =  Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = DIMENS_16dp, end = DIMENS_16dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically // Add this line to center the elements vertically
                ) {
                    Text(
                        text = "注文詳細(テーブル＃${order.tableId})",
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TEXT_SIZE_18sp,
                        color = Black
                    )
                    Text(
                        text = "時間：$formattedDate",
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = TEXT_SIZE_16sp,
                        color = Black
                    )
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(DIMENS_2dp),
                    contentPadding = PaddingValues(DIMENS_8dp)
                ) {
                    item {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            TableCell(text = "イメージ", weight = column3Weight, title = true,alignment = TextAlign.Left,
                            )
                            TableCell(text = "料理名", weight = column4Weight, title = true)
                            TableCell(
                                text = "数",
                                weight = column5Weight,
                                alignment = TextAlign.Right,
                                title = true
                            )
                        }
                        Divider(
                            color = GraySecondTextColor,
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxHeight()
                                .fillMaxWidth()
                        )
                    }

                    itemsIndexed(order.product!!) { index, product ->
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            TableCell(text ="", weight = column3Weight,alignment = TextAlign.Left)
                            TableCell(
                                text = product.prodName!!,
                                weight = column4Weight,
                            )
                            TableCell(text = "x" + order.orderProduct!![index]!!.quantity!!.toString(), weight = column5Weight,alignment = TextAlign.Right)
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

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        modifier = Modifier.sizeIn(minWidth = 300.dp, minHeight = 100.dp),
                        onClick = {
                            viewModel.updateOrderStatus(order.id!!,"提供済み")

                            if (viewModel.checkedOrderList.value.isEmpty()) {
                                navController.navigate(Screen.OrderListScreen.route)
                                viewModel.mRobot.goTo("ホームベース")
                            } else {
                                viewModel.processCheckedRow(navController)
                            }
                        }
                    ) {
                        Text(text = "受け取った", color = White, fontSize = 80.sp)
                    }

                }
            }


        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ListContentProductPreview() {
//    CustomerScreen(
//        order = OrderModelItem(
//            orderProduct = listOf(OrderProduct(3)),
//            product = listOf(Product(catId = "ds", prodName = "カルビ", prodAvail = true, prodDesc = "edd", prodId = "fdf", prodImage = "redf", prodPrice = 23)),
//            id="2",
//            status = "DS",
//            tableId = "3",
//            time = "2023-08-02T09:53:13.405539+09:00"
//        )
//
//    )
//}



//@Composable
//fun ProductList(products: List<Product>) {
//    Column(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = "Products",
//            fontFamily = GilroyFontFamily,
//            fontWeight = FontWeight.SemiBold,
//            fontSize = TEXT_SIZE_18sp,
//            color = Black,
//            modifier = Modifier.padding(vertical = DIMENS_8dp, horizontal = DIMENS_16dp)
//        )
//
//        LazyRow(
//            horizontalArrangement = Arrangement.spacedBy(DIMENS_4dp)
//        ) {
//            items(products) { product ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = DIMENS_16dp, vertical = DIMENS_4dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    product.prodName?.let {
//                        Text(
//                            text = it,
//                            fontFamily = GilroyFontFamily,
//                            fontWeight = FontWeight.Normal,
//                            fontSize = TEXT_SIZE_16sp,
//                            color = Black
//                        )
//                    }
//                    Text(
//                        text = "￥" + product.prodPrice,
//                        fontFamily = GilroyFontFamily,
//                        fontWeight = FontWeight.Normal,
//                        fontSize = TEXT_SIZE_16sp,
//                        color = Black
//                    )
//                }
//            }
//        }
//    }
//}

