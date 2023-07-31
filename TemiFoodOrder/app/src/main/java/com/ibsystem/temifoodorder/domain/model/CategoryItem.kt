package com.ibsystem.temifoodorder.domain.model

import androidx.compose.ui.graphics.Color

data class CategoryItem(
    val cat_id: String,
    val cat_name: String,
    val image: Int,
    val background: Color
)
