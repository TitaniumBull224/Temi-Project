package com.ibsystem.temifooddelivery.presentation.common.content

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import com.ibsystem.temifooddelivery.domain.OrderProduct
import com.ibsystem.temifooddelivery.domain.Product
import com.ibsystem.temifooddelivery.presentation.common.card.ProductCard
import com.ibsystem.temifooddelivery.presentation.screen.order_list.OrderViewModel
import com.ibsystem.temifooddelivery.ui.theme.*
import com.ibsystem.temifooddelivery.utils.reformatDate
import kotlinx.coroutines.launch

val column1Weight = .1f
val column2Weight = .2f
val column3Weight = .3f
val column4Weight = .3f
val column5Weight = .1f

@SuppressLint("NewApi")
@Composable
fun ListOrder(
    modifier: Modifier = Modifier,
    title: String,
    orders: List<OrderModelItem>,
    viewModel: OrderViewModel,
    navController: NavController
) {
    val checkedState = remember { mutableStateListOf<Boolean>() }
    checkedState.clear()
    checkedState.addAll(List(orders.size) { false })

    val checkedRowIds = remember { mutableStateListOf<String>() }
    checkedRowIds.clear()

    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DIMENS_16dp, end = DIMENS_16dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically // Add this line to center the elements vertically
        ) {
            Text(
                text = title,
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = TEXT_SIZE_24sp,
                color = Black
            )
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TableCell(text = "", weight = column1Weight)
            TableCell(
                text = "テーブル",
                weight = column2Weight,
                alignment = TextAlign.Left,
                title = true
            )
            TableCell(text = "時間", weight = column3Weight, title = true)
            TableCell(text = "状況", weight = column4Weight, title = true)
            TableCell(
                text = "",
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

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(orders) {
            coroutineScope.launch {
                listState.animateScrollToItem(orders.size)

            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(DIMENS_2dp),
            contentPadding = PaddingValues(DIMENS_8dp),
            state = listState,
        ) {
            itemsIndexed(orders) { index, order ->
                val isRowExpanded = remember { mutableStateOf(false) }

                val formattedDate = reformatDate(order.time!!)

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { isRowExpanded.value = !isRowExpanded.value },
                        modifier = Modifier
                            .padding(10.dp)
                            .weight(column1Weight)
                    ) {
                        Icon(
                            imageVector = if (isRowExpanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    TableCell(
                        text = "#" + order.tableId!!,
                        weight = column2Weight,
                        alignment = TextAlign.Left
                    )
                    TableCell(text = formattedDate, weight = column3Weight)
                    StatusCell(text = order.status!!, weight = column4Weight)
                    CheckBoxCell(
                        weight = column5Weight,
                        checked = checkedState[index],
                        enabled = order.status == "保留中",
                        onCheckedChange = { checked ->
                            checkedState[index] = checked
                            if (checked) {
                                viewModel.addCheckedOrderList(order.id!!) // Add the checked row ID to the viewModel.checkedOrderList
                            } else {
                                viewModel.removeCheckedOrderList(order.id!!) // Remove the unchecked row ID from the viewModel.checkedOrderList
                            }
                        }
                    )
                }
                Divider(
                    color = GraySecondTextColor,
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxHeight()
                        .fillMaxWidth()
                )

                // Conditionally display the expanded row with product list
                if (isRowExpanded.value) {
                    order.product?.let { ProductList(products = it, prodQuantity = order.orderProduct!!) } // Assuming `products` is a list of ProductItem inside the OrderModelItem
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = DIMENS_16dp, end = DIMENS_16dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Button(
                        onClick = {
                            if (viewModel.checkedOrderList.value.isNotEmpty()) {
                                viewModel.processCheckedRow(navController)
                            } else {
                                Toast.makeText(context, "オーダーを選んでください", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.padding(DIMENS_16dp)
                    ) {
                        Text(text = "準備完了", color = White)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductList(products: List<Product>, prodQuantity: List<OrderProduct?>) {
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
                    ProductCard(product = product, quantity = prodQuantity[index]!!.quantity!!)

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

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(10.dp),
        fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
        textAlign = alignment
    )
}

@Composable
fun RowScope.StatusCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
) {

    val color = when (text) {
        "保留中" -> Color(0xFFFF9C00)
        "準備完了" -> Color(0xFF0090FF)
        "提供済み" -> Color(0xFF00BCD4)
        "完了" -> Color(0xFF1CFF00)
        else -> Color(0xffffcccf)
    }
    val textColor = when (text) {
        "保留中" -> Color(0xFFFFFFFF) //Pending
        "準備完了" -> Color(0xFFFFFFFF) //Prepared
        "提供済み" -> Color(0xFFFFFFFF) //Served
        "完了" -> Color(0xFFFFFFFF) //Paid
        else -> Color(0xffca1e17)
    }

    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(12.dp)
            .background(color, shape = RoundedCornerShape(50.dp)),
        textAlign = alignment,
        color = textColor
    )
}

@Composable
fun RowScope.CheckBoxCell(
    checked: Boolean = false,
    weight: Float,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    if (enabled) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .weight(weight)
                .padding(10.dp)
        )
    } else {
        Box(
            modifier = Modifier
                .weight(weight)
                .padding(10.dp)
        ) {}

    }
}