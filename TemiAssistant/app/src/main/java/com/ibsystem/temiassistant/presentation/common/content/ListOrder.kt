package com.ibsystem.temiassistant.presentation.common.content

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.ibsystem.temiassistant.domain.model.OrderModelItem
import com.ibsystem.temiassistant.domain.model.OrderProduct
import com.ibsystem.temiassistant.domain.model.Product
import com.ibsystem.temiassistant.presentation.common.card.ProductCard
import com.ibsystem.temiassistant.presentation.common.component.ButtonGradient
import com.ibsystem.temiassistant.presentation.common.component.TextGradient
import com.ibsystem.temiassistant.presentation.screen.order_list.OrderViewModel
import com.ibsystem.temiassistant.ui.theme.*
import com.ibsystem.temiassistant.utils.reformatDate
import kotlinx.coroutines.launch

const val column1Weight = .1f
const val column2Weight = .2f
const val column3Weight = .3f
const val column4Weight = .3f
const val column5Weight = .1f

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
//            TableCell(
//                text = "テーブル",
//                weight = column2Weight,
//                alignment = TextAlign.Left,
//                title = true
//            )
//            TableCell(text = "時間", weight = column3Weight, title = true)
//            TableCell(text = "状況", weight = column4Weight, title = true)
            TextGradient(
                text = "テーブル",
                modifier = Modifier.weight(column2Weight)
            )
            TextGradient(
                text = "時間",
                modifier = Modifier.weight(column3Weight)
            )
            TextGradient(
                text = "状況",
                modifier = Modifier.weight(column4Weight)
            )
            TableCell(
                text = "",
                weight = column5Weight,
                alignment = TextAlign.Right,
                title = true
            )
        }
        Divider(
            color = Gray,
            modifier = Modifier
                .height(DIMENS_1dp)
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
                            .padding(DIMENS_10dp)
                            .weight(column1Weight)
                    ) {
                        Icon(
                            imageVector = if (isRowExpanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = White
                        )
                    }
//                    TableCell(
//                        text = "#" + order.tableId!!,
//                        weight = column2Weight,
//                        alignment = TextAlign.Left
//                    )
//                    TableCell(text = formattedDate, weight = column3Weight)
                    TextGradient(
                        text = "#" + order.tableId!!,
                        modifier = Modifier.weight(column2Weight)
                    )
                    TextGradient(
                        text = formattedDate,
                        modifier = Modifier.weight(column3Weight)
                    )
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
                    color = Gray,
                    modifier = Modifier
                        .height(DIMENS_1dp)
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
//                    Button(
//                        onClick = {
//                            if (viewModel.checkedOrderList.value.isNotEmpty()) {
//                                viewModel.processCheckedRow(navController)
//                            } else {
//                                Toast.makeText(context, "オーダーを選んでください", Toast.LENGTH_SHORT).show()
//                            }
//                        },
//                        modifier = Modifier.padding(DIMENS_16dp)
//                    ) {
//                        Text(text = "準備完了", color = White)
//                    }
                    ButtonGradient(
                        name = "準備完了",
                        onClick = {
                            if (viewModel.checkedOrderList.value.isNotEmpty()) {
                                viewModel.processCheckedRow(navController)
                            } else {
                                Toast.makeText(context, "オーダーを選んでください", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
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
                    color = Gray,
                    modifier = Modifier
                        .height(DIMENS_1dp)
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
            .padding(DIMENS_10dp),
        fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
        textAlign = alignment,
        color = Black
    )
//    TextGradient(
//        modifier = Modifier.weight(weight),
//        text = text
//    )
}

@Composable
fun RowScope.StatusCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
) {

    val color = when (text) {
        "保留中" -> DarkOrange
        "準備完了" -> DarkBlue
        "提供済み" -> Teal
        "完了" -> DarkGreen
        else -> DeepPink
    }
    val textColor = when (text) {
        "保留中" -> White //Pending
        "準備完了" -> White //Prepared
        "提供済み" -> White //Served
        "完了" -> White //Paid
        else -> Crimson // similar to Color(0xffca1e17)
    }

    Box(
        modifier = Modifier
            .weight(weight),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = alignment,
            color = textColor,
            modifier = Modifier
                .background(color, shape = RoundedCornerShape(DIMENS_50dp))
                .padding(DIMENS_12dp)
        )
    }

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
            colors = CheckboxDefaults.colors(
                checkedColor = Maroon,
                uncheckedColor = White
            ),
            modifier = Modifier
                .weight(weight)
                .padding(DIMENS_10dp)
        )
    } else {
        Box(
            modifier = Modifier
                .weight(weight)
                .padding(DIMENS_10dp)
        ) {}

    }
}