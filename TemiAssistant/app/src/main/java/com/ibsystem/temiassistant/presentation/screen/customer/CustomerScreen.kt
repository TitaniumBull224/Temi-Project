package com.ibsystem.temiassistant.presentation.screen.customer


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ibsystem.temiassistant.R
import com.ibsystem.temiassistant.navigation.Screen
import com.ibsystem.temiassistant.presentation.common.card.ProductCard
import com.ibsystem.temiassistant.presentation.common.component.GifImage
import com.ibsystem.temiassistant.presentation.common.component.TextGradient
import com.ibsystem.temiassistant.presentation.common.content.TableCell
import com.ibsystem.temiassistant.presentation.common.content.column1Weight
import com.ibsystem.temiassistant.presentation.common.content.column2Weight
import com.ibsystem.temiassistant.presentation.common.content.column3Weight
import com.ibsystem.temiassistant.presentation.common.content.column4Weight
import com.ibsystem.temiassistant.presentation.common.content.column5Weight
import com.ibsystem.temiassistant.presentation.screen.order_list.OrderViewModel
import com.ibsystem.temiassistant.ui.theme.Black
import com.ibsystem.temiassistant.ui.theme.DIMENS_16dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_24dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_2dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_4dp
import com.ibsystem.temiassistant.ui.theme.DIMENS_8dp
import com.ibsystem.temiassistant.ui.theme.GilroyFontFamily
import com.ibsystem.temiassistant.ui.theme.GraySecondTextColor
import com.ibsystem.temiassistant.ui.theme.TEXT_SIZE_16sp
import com.ibsystem.temiassistant.ui.theme.TEXT_SIZE_18sp
import com.ibsystem.temiassistant.ui.theme.TEXT_SIZE_24sp
import com.ibsystem.temiassistant.ui.theme.White
import com.ibsystem.temiassistant.utils.reformatDate

@Composable
fun CustomerScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    orderID: String,
    viewModel: OrderViewModel
) {
    val order = viewModel.findOrderByID(orderID)
    order?.let {
        val formattedDate = reformatDate(order.time!!)
        GifImage(
            modifier = Modifier.fillMaxSize(),
            gif = R.drawable.lantern
        )

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
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
//                        Text(
//                            text = "注文詳細",
//                            fontFamily = GilroyFontFamily,
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = TEXT_SIZE_24sp,
//                            color = Black
//                        )
                        TextGradient(text = "注文詳細")

                        Spacer(modifier = Modifier.height(DIMENS_24dp))

                        TextGradient(text = "${order.tableId}")

//                        Text(
//                            text = "テーブル：${order.tableId}",
//                            fontFamily = GilroyFontFamily,
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = TEXT_SIZE_18sp,
//                            color = Black
//
//                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Top
                    ) {
//                        Text(
//                            text = "時間：$formattedDate",
//                            fontFamily = GilroyFontFamily,
//                            fontWeight = FontWeight.Light,
//                            fontSize = TEXT_SIZE_16sp,
//                            color = Black
//                        )
                        TextGradient(text = "時間：$formattedDate")
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(DIMENS_4dp)
                ) {
                    itemsIndexed(order.product!!) { index, product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = DIMENS_16dp, vertical = DIMENS_4dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductCard(
                                product = product,
                                quantity = order.orderProduct!![index]!!.quantity!!
                            )
                        }
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

@Preview(showBackground = true)
@Composable
fun something() {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
//                    Text(
//                        text = "注文詳細",
//                        fontFamily = GilroyFontFamily,
//                        fontWeight = FontWeight.SemiBold,
//                        fontSize = TEXT_SIZE_24sp,
//                        color = Black
//                    )
                    TextGradient(text = "注文詳細")

                    Spacer(modifier = Modifier.height(DIMENS_24dp))

                    Text(
                        text = "テーブル：2",
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TEXT_SIZE_18sp,
                        color = Black

                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "時間：00",
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = TEXT_SIZE_16sp,
                        color = Black
                    )
                }



            }
        }
    }
}

