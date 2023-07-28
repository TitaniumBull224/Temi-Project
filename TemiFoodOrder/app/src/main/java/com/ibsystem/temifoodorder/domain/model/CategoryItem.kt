package com.ibsystem.temifoodorder.domain.model

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
data class CategoryItem(
    val cat_id: String,
    val cat_name: String,
    val image: String,
    val background: String
)
