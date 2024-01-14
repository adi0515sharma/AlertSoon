package com.kft.emojiex.ui.model

import androidx.compose.ui.graphics.Color

data class TaskUI(
    val background : Color,
    val textColor : Color,
    val timeColor : Color,
    val dividerColor : Color,
    val selectedDay : Color,
    val unselectedDay : Color,
    val deleteIconTint : Color,
    val deleteIconBg : Color,
    val snoozeIcon : Color
)
