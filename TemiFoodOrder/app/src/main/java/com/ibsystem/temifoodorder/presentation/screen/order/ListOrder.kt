package com.ibsystem.temifoodorder.presentation.screen.order

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ibsystem.temifoodorder.domain.model.OrderDetailItem
import com.ibsystem.temifoodorder.presentation.common.content.ListOrderProduct
import com.ibsystem.temifoodorder.ui.theme.Black
import com.ibsystem.temifoodorder.ui.theme.DIMENS_16dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_2dp
import com.ibsystem.temifoodorder.ui.theme.DIMENS_8dp
import com.ibsystem.temifoodorder.ui.theme.GilroyFontFamily
import com.ibsystem.temifoodorder.ui.theme.GraySecondTextColor
import com.ibsystem.temifoodorder.ui.theme.TEXT_SIZE_24sp
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
    orders: List<OrderDetailItem>,
//    viewModel: OrderViewModel,
//    navController: NavController,
//    onClickToCart: (ProductItem) -> Unit
) {

    val checkedState = remember { mutableStateListOf<Boolean>() }
    checkedState.clear()
    checkedState.addAll(List(orders.size) { false })

    val checkedRowIds = remember { mutableStateListOf<String>() }
    checkedRowIds.clear()



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
            item {
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
                    TableCell(text = "デート", weight = column3Weight, title = true)
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
            }

            itemsIndexed(orders) { index, order ->
                val isRowExpanded = remember { mutableStateOf(false) }


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
                    TableCell(text = order.time!!, weight = column3Weight)
                    StatusCell(text = order.status!!, weight = column4Weight)
                    TableCell(text = "", weight = column5Weight)
//                    CheckBoxCell(
//                        weight = column5Weight,
//                        checked = checkedState[index],
//                        enabled = order.status == "保留中",
//                        onCheckedChange = { checked ->
//                            checkedState[index] = checked
//                            if (checked) {
//                                checkedRowIds.add(order.id!!) // Add the checked row ID to the list
//                            } else {
//                                checkedRowIds.remove(order.id!!) // Remove the unchecked row ID from the list
//                            }
//                        }
//                    )
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
                        order.product?.let { ListOrderProduct(products = it, prodQuantity = order.orderProduct!!) } // Assuming `products` is a list of ProductItem inside the OrderModelItem
                }
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
