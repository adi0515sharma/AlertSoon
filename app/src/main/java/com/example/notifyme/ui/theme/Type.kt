package com.example.AlertSoon.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.AlertSoon.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val primary_font = FontFamily(Font(resId = R.font.poppins_regular))

val CompactTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    titleMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),

    labelSmall = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = primary_font,
        fontSize = 18.sp,
        fontWeight = FontWeight.W500,
    ),
    labelLarge = TextStyle(
        fontFamily = primary_font,
        fontSize = 20.sp,
        fontWeight = FontWeight.W700
    ),
    displayMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 40.sp
    ),
    displayLarge = TextStyle(
        fontSize  = 120.sp
    ),
    bodySmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = primary_font
    )

)

val CompactMediumTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600
    ),
    titleSmall = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    ),
    titleMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp
    ),
    titleLarge = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),

    labelSmall = TextStyle(
        fontFamily = primary_font,

        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    ),
    labelMedium = TextStyle(
        fontFamily = primary_font,
        fontSize = 15.sp,
        fontWeight = FontWeight.W500,
    ),
    labelLarge = TextStyle(
        fontFamily = primary_font,
        fontSize = 17.sp,
        fontWeight = FontWeight.W700
    ),
    displayMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 35.sp
    ),
    displayLarge = TextStyle(
        fontSize  = 100.sp
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        fontFamily = primary_font
    )
)

val CompactSmallTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W500,
        fontSize = 10.sp
    ),
    titleMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 12.sp
    ),
    titleLarge = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp
    ),

    labelSmall = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp
    ),
    labelMedium = TextStyle(
        fontFamily = primary_font,
        fontSize = 13.sp,
        fontWeight = FontWeight.W500,
    ),
    labelLarge = TextStyle(
        fontFamily = primary_font,
        fontSize = 15.sp,
        fontWeight = FontWeight.W700
    ),
    displayMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 30.sp
    ),
    displayLarge = TextStyle(
        fontSize = 80.sp
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = primary_font
    )

)

val MediumTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W700,
        fontSize = 30.sp
    ),
    titleSmall = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W500,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 32.sp
    ),

    labelSmall = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    labelMedium = TextStyle(
        fontFamily = primary_font,
        fontSize = 26.sp,
        fontWeight = FontWeight.W500,
    ),
    labelLarge = TextStyle(
        fontFamily = primary_font,
        fontSize = 28.sp,
        fontWeight = FontWeight.W700
    ),
    displayMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 49.sp
    ),
    displayLarge = TextStyle(
        fontSize  = 180.sp
    ),
    bodySmall = TextStyle(
        fontSize = 22.sp,
        fontFamily = primary_font
    )


)

val ExpandedTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W900,
        fontSize = 40.sp
    ),
    titleSmall = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W500,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 32.sp
    ),

    labelSmall = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp
    ),
    labelMedium = TextStyle(
        fontFamily = primary_font,
        fontSize = 32.sp,
        fontWeight = FontWeight.W500,
    ),
    labelLarge = TextStyle(
        fontFamily = primary_font,
        fontSize = 34.sp,
        fontWeight = FontWeight.W700
    ),
    displayMedium = TextStyle(
        fontFamily = primary_font,
        fontWeight = FontWeight.W600,
        fontSize = 50.sp
    ),
    displayLarge = TextStyle(
        fontSize  = 130.sp
    ),
    bodySmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = primary_font
    )

)