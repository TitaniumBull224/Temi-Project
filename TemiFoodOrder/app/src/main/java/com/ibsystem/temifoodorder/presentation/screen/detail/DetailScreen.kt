package com.ibsystem.temifoodorder.presentation.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ibsystem.temifoodorder.R
import com.ibsystem.temifoodorder.domain.model.ProductItem
import com.ibsystem.temifoodorder.presentation.common.SpacerDividerContent
import com.ibsystem.temifoodorder.presentation.component.RatingBar
import com.ibsystem.temifoodorder.ui.theme.*
import com.ibsystem.temifoodorder.utils.showToastShort

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel(),
) {
    val mContext = LocalContext.current
    val selectedProduct by detailViewModel.selectedProduct.collectAsState()

    Scaffold { padding ->
        Column {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
                    .padding(padding)
            ) {
                selectedProduct?.let { productItem ->
                    DetailContentImageHeader(productItem = productItem)

                    Spacer(modifier = Modifier.height(DIMENS_24dp))

                    DetailContentDescription(productItem = productItem)
                }
            }

            Column {
                selectedProduct?.let {
                    DetailButtonAddCart(
                        productItem = it,
                        onClickToCart = { productItem ->
                            mContext.showToastShort("Success Add To Cart ${productItem.prodName}")
                            // TODO: Add select Cart
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DetailContentImageHeader(
    productItem: ProductItem
) {
    Card(
        shape = RoundedCornerShape(bottomEnd = DIMENS_24dp, bottomStart = DIMENS_24dp),
        backgroundColor = GrayBackground,
        modifier = Modifier
            .blur(DIMENS_1dp)
            .fillMaxWidth(),
    ) {
//        Image(
//            painter = painterResource(id = productItem.prodImage),
//            contentDescription = stringResource(id = R.string.image_product),
//            modifier = Modifier.height(DIMENS_353dp)
//        )
    }
}

@Composable
fun DetailContentDescription(
    modifier: Modifier = Modifier,
    productItem: ProductItem
) {
    Column(
        modifier = modifier.padding(start = DIMENS_16dp, end = DIMENS_16dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = productItem.prodName!!,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    fontSize = TEXT_SIZE_24sp
                )

            }
            Icon(
                painter = painterResource(id = R.drawable.ic_favorite_border),
                contentDescription = stringResource(R.string.image_favorite),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(DIMENS_8dp))

        Text(
            text = "$${productItem.prodPrice}",
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.Bold,
            color = Black,
            modifier = Modifier.align(Alignment.End),
            fontSize = TEXT_SIZE_18sp
        )

        SpacerDividerContent()

        Text(
            text = stringResource(R.string.product_detail),
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = Black,
            fontSize = TEXT_SIZE_16sp,
        )

        Spacer(modifier = Modifier.height(DIMENS_8dp))

        Text(
            text = productItem.prodDesc!!,
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.Medium,
            color = GraySecondTextColor,
            fontSize = TEXT_SIZE_12sp,
        )

        Spacer(modifier = Modifier.height(DIMENS_16dp))
        SpacerDividerContent()

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.nutritions),
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Black,
                fontSize = TEXT_SIZE_16sp,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            Card(
                shape = RoundedCornerShape(DIMENS_6dp),
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Something Something",
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = GraySecondTextColor,
                    fontSize = TEXT_SIZE_10sp,
                    modifier = Modifier
                        .background(color = GrayBackgroundSecond)
                        .padding(DIMENS_4dp)
                )
            }

            Spacer(modifier = Modifier.width(DIMENS_8dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.arrow_right)
            )
        }

        SpacerDividerContent()

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.review),
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Black,
                fontSize = TEXT_SIZE_16sp,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            RatingBar(rating = 4.3)

            Spacer(modifier = Modifier.width(DIMENS_8dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.arrow_right)
            )
        }

    }
}

@Composable
fun DetailButtonAddCart(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
    onClickToCart: (ProductItem) -> Unit
) {
    Button(
        shape = RoundedCornerShape(DIMENS_24dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Green),
        modifier = modifier
            .fillMaxWidth()
            .padding(DIMENS_16dp),
        onClick = { onClickToCart.invoke(productItem) }
    ) {
        Text(
            text = stringResource(R.string.add_to_cart),
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = TEXT_SIZE_18sp,
            color = Color.White,
            modifier = Modifier.padding(top = DIMENS_8dp, bottom = DIMENS_8dp)
        )
    }
}


@Preview
@Composable
fun DetailScreenImageHeaderPreview() {
    DetailContentImageHeader(
        ProductItem(
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