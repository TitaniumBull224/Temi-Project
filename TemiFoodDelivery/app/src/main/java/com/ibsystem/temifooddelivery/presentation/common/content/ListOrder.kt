package com.ibsystem.temifooddelivery.presentation.common.content

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ibsystem.temifooddelivery.R
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import com.ibsystem.temifooddelivery.domain.Product
import com.ibsystem.temifooddelivery.ui.theme.*
import kotlinx.coroutines.launch

val column1Weight = .2f
val column2Weight = .3f
val column3Weight = .25f
val column4Weight = .25f

@Composable
fun ListOrder(
    modifier: Modifier = Modifier,
    title: String,
    orders: List<OrderModelItem>,
    //navController: NavController,
//    onClickToCart: (ProductItem) -> Unit
) {


    val checkedState = remember { mutableStateListOf<Boolean>() }
    checkedState.clear()
    checkedState.addAll(List(orders.size) { false })

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(orders) {
        coroutineScope.launch {
            listState.animateScrollToItem(orders.size)
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
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
            Button(
                onClick = { /* Handle button click */ },
                modifier = Modifier.padding(DIMENS_16dp)
            ) {
                Text(text = "Click Me", color = White)
            }
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
                    TableCell(
                        text = "テーブル",
                        weight = column1Weight,
                        alignment = TextAlign.Left,
                        title = true
                    )
                    TableCell(text = "デート", weight = column2Weight, title = true)
                    TableCell(text = "状況", weight = column3Weight, title = true)
                    TableCell(
                        text = "Amount",
                        weight = column4Weight,
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { isRowExpanded.value = !isRowExpanded.value },
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isRowExpanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    TableCell(
                        text = "#" + order.tableId!!,
                        weight = column1Weight,
                        alignment = TextAlign.Left
                    )
                    TableCell(text = order.time!!, weight = column2Weight)
                    StatusCell(text = "Pending", weight = column3Weight)
                    CheckBoxCell(
                        weight = column4Weight,
                        checked = checkedState[index],
                        onCheckedChange = { checked ->
                            checkedState[index] = checked
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
                    order.product?.let { ProductList(products = it) } // Assuming `products` is a list of ProductItem inside the OrderModelItem
                }
            }
        }
    }
}

@Composable
fun ProductList(products: List<Product>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Products",
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = TEXT_SIZE_18sp,
            color = Black,
            modifier = Modifier.padding(vertical = DIMENS_8dp, horizontal = DIMENS_16dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(DIMENS_4dp)
        ) {
            items(products) { product ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = DIMENS_16dp, vertical = DIMENS_4dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    product.prodName?.let {
                        Text(
                            text = it,
                            fontFamily = GilroyFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = TEXT_SIZE_16sp,
                            color = Black
                        )
                    }
                    Text(
                        text = "￥" + product.prodPrice,
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = TEXT_SIZE_16sp,
                        color = Black
                    )
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
        textAlign = alignment,
    )
}

@Composable
fun RowScope.StatusCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
) {

    val color = when (text) {
        "Pending" -> Color(0xfff8deb5)
        "Paid" -> Color(0xffadf7a4)
        else -> Color(0xffffcccf)
    }
    val textColor = when (text) {
        "Pending" -> Color(0xffde7a1d)
        "Paid" -> Color(0xff00ad0e)
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
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = Modifier
            .weight(weight)
            .padding(10.dp)
    )
}

//@Preview(showBackground = true)
//@Composable
//fun ListContentProductPreview() {
//    ListContentProduct(
//        title = "Exclusive Offer",
//        products = listOf(
//            ProductItem(
//                id = "1",
//                name = "Organic Bananas",
//                description = "",
//                image = R.drawable.product10,
//                price = 4.99
//            ),
//            ProductItem(
//                id = "2",
//                name = "Organic Bananas",
//                description = "",
//                image = R.drawable.product10,
//                price = 4.99
//            ),
//            ProductItem(
//                id = "3",
//                name = "Organic Bananas",
//                description = "",
//                image = R.drawable.product10,
//                price = 4.99
//            )
//        ),
//        navController = rememberNavController(),
//        onClickToCart = {}
//    )
//}