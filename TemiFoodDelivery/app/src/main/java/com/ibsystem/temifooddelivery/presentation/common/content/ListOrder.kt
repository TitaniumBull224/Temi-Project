package com.ibsystem.temifooddelivery.presentation.common.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ibsystem.temifooddelivery.R
import com.ibsystem.temifooddelivery.domain.OrderModelItem
import com.ibsystem.temifooddelivery.presentation.common.card.ExpandableCard
import com.ibsystem.temifooddelivery.ui.theme.*

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
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DIMENS_16dp, end = DIMENS_16dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = TEXT_SIZE_24sp,
                color = Black
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(id = R.string.see_all),
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = TEXT_SIZE_12sp,
                color = Green
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(DIMENS_2dp),
            contentPadding = PaddingValues(DIMENS_8dp)
        ) {
//            items(orders) { order ->
//                ExpandableCard(
//                    orderItem = order
//                )
//            }
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
//                    TableCell(
//                        text = "Amount",
//                        weight = column4Weight,
//                        alignment = TextAlign.Right,
//                        title = true
//                    )
                }
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxHeight()
                        .fillMaxWidth()
                )
            }

            itemsIndexed(orders) { _, order ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TableCell(
                        text = "#"+order.tableId!!,
                        weight = column1Weight,
                        alignment = TextAlign.Left
                    )
                    TableCell(text = order.time!!, weight = column2Weight)
                    StatusCell(text = "Pending", weight = column3Weight)
//                    TableCell(
//                        text = "order",
//                        weight = column4Weight,
//                        alignment = TextAlign.Right
//                    )
                }
                Divider(
                    color = Color.LightGray,
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