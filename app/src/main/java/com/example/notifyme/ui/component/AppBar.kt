package com.example.notifyme.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notifyme.R
import com.example.notifyme.ui.navigation.FeatureNavScreen

@Composable
fun AppBar(navController: NavHostController? = null) {

    val context = LocalContext.current
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.primary)
        .padding(vertical = 15.dp, horizontal = 10.dp),

        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = context.resources.getString(R.string.app_name),
            fontFamily = FontFamily(
                Font(R.font.poppins_regular)
            ),
            fontWeight = FontWeight.W600,
            color = Color.White,
        )

        if(navController!=null){

            Icon(imageVector = Icons.Default.Add,modifier = Modifier.clickable {
                navController.navigate(FeatureNavScreen.CREATING_TASK.name)
            }.padding(3.dp), contentDescription = null, tint = Color.White)
        }

    }
//    TopAppBar(
//        title = {
//            Text(
//                text = context.resources.getString(R.string.app_name),
//                fontFamily = FontFamily(
//                    Font(R.font.poppins_regular)),
//                fontWeight = FontWeight.W600,
//                color = Color.White,
//                )
//        },
//        backgroundColor = MaterialTheme.colorScheme.primary,
//        elevation = 0.dp,
//        modifier = Modifier.fillMaxWidth(),
//    )
}