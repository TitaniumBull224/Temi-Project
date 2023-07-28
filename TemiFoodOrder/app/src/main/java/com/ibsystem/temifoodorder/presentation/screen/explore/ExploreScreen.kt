package com.ibsystem.temifoodorder.presentation.screen.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ibsystem.temifoodorder.R
import com.ibsystem.temifoodorder.presentation.common.card.CategoryCard
import com.ibsystem.temifoodorder.presentation.component.SearchViewBar
import com.ibsystem.temifoodorder.ui.theme.*
import com.ibsystem.temifoodorder.utils.DataDummy

@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier
) {
    Scaffold { paddingValues ->
        Column(
            modifier = modifier.padding(paddingValues)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = DIMENS_16dp),
                text = stringResource(R.string.find_products),
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = TEXT_SIZE_18sp,
                color = Black
            )

            SearchViewBar(hint = stringResource(id = R.string.search_category))

            LazyVerticalGrid(
                modifier = Modifier.padding(start = DIMENS_16dp, end = DIMENS_16dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(DIMENS_10dp),
                horizontalArrangement = Arrangement.spacedBy(DIMENS_12dp),
            ) {
                items(DataDummy.generateDummyCategories()) { categoryItem ->
                    CategoryCard(categoryItem = categoryItem)
                }
            }
        }
    }
}

@Preview
@Composable
fun ExploreScreenPreview() {
    ExploreScreen()
}