package com.alertSoon.alarm.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.alertSoon.alarm.R
import com.alertSoon.alarm.ui.navigation.FeatureNavScreen
import com.alertSoon.alarm.ui.theme.dimens

@Composable
fun AppBar(navController: NavHostController? = null) {

    val context = LocalContext.current
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.primary)
        .padding(vertical = MaterialTheme.dimens.app_bar_vertical_padding, horizontal = MaterialTheme.dimens.app_bar_horizontal_padding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = context.resources.getString(R.string.app_name),
            fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            color = Color.White,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )

        if(navController!=null){

            Icon(imageVector = Icons.Default.Add,modifier =
            Modifier.clickable {
                navController.navigate(FeatureNavScreen.CREATING_TASK.name)
            }
                .size(MaterialTheme.dimens.home_screen_create_new_task_icon_size)
                .padding(MaterialTheme.dimens.app_bar_action_button_all_padding), contentDescription = null, tint = Color.White)
        }

    }

}