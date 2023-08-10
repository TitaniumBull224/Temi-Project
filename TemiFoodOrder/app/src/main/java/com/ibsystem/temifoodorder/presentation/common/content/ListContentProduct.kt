package com.ibsystem.temifoodorder.presentation.common.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.ibsystem.temifoodorder.R
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.presentation.common.card.ProductCard
import com.ibsystem.temifoodorder.ui.theme.*

@Composable
fun ListContentProduct(
    modifier: Modifier = Modifier,
    title: String,
    products: List<ProductItem>,
    navController: NavController,
    onClickToCart: (ProductItem) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {


        Spacer(modifier = Modifier.height(DIMENS_24dp))
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
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(DIMENS_2dp),
            contentPadding = PaddingValues(DIMENS_8dp)
        ) {
            items(products) { product ->
                ProductCard(
                    productItem = product,
                    navController = navController,
                    onClickToCart = onClickToCart
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListContentProductPreview() {
    ListContentProduct(
        title = "商品",
        products = listOf(
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
            ProductItem(
                catId = "aishhvaisuhg",
                prodAvail = true,
                prodDesc = "god damn",
                prodId = "foauhvauhuahg",
                prodImage = "https://burpple-2.imgix.net/foods/18701ea9eb80bcd299c1559365_original.",
                prodPrice = 598,
                prodName = "焼肉"
            )
        ),
        navController = rememberNavController(),
        onClickToCart = {}
    )
}