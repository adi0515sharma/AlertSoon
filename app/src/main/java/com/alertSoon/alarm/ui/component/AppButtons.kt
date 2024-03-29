package com.alertSoon.alarm.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.alertSoon.alarm.R
import com.alertSoon.alarm.ui.theme.dimens
import java.util.Locale

@Composable
fun AppButton(
    modifier: Modifier,
    text : String,
    handleClick: HandleClick,
isError : Boolean = false){

    Button(
        onClick = {
            handleClick.onClick()
        },
        colors = ButtonDefaults.outlinedButtonColors(containerColor = if(!isError) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onErrorContainer),
        modifier = modifier.height(MaterialTheme.dimens.button_height)
    ) {
        Text(
            text = text.toUpperCase(),
            color = if(!isError) Color.White else MaterialTheme.colorScheme.onError,
            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
            fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
            fontSize = MaterialTheme.typography.titleSmall.fontSize,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
}



public interface HandleClick{
    public fun onClick()
}