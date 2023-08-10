package com.ibsystem.temifoodorder.presentation.common.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ibsystem.temifoodorder.R
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.ui.theme.*

@Composable
fun FavoriteCard(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .padding(DIMENS_16dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
//            Image(
//                painter = painterResource(id = productItem.prodImage),
//                contentDescription = stringResource(id = R.string.image_product)
//            )

            Spacer(modifier = Modifier.padding(start = DIMENS_16dp))

            Column(
                modifier = Modifier
                    .weight(1F)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = productItem.prodName!!,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    fontSize = TEXT_SIZE_16sp,
                )

            }

            Row(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "￥${productItem.prodPrice}",
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    fontSize = TEXT_SIZE_16sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.arrow_right)
                )
            }
        }

        Spacer(modifier = Modifier.height(DIMENS_20dp))

        Divider(modifier = Modifier.height(DIMENS_1dp), color = GrayBorderStroke)
    }
}

@Preview
@Composable
fun FavoriteCardPreview() {
    FavoriteCard(
        productItem = ProductItem(
            catId = "aishhvaisuhg",
            prodAvail = true,
            prodDesc = "god damn",
            prodId = "foauhvauhuahg",
            prodImage = "https://burpple-2.imgix.net/foods/18701ea9eb80bcd299c1559365_original.",
            prodPrice = 598,
            prodName = "焼肉"
        )
    )
}