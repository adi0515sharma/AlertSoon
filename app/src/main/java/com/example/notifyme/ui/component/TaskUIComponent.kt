package com.example.AlertSoon.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.example.AlertSoon.R
import com.example.AlertSoon.ui.local_storage.Task.TableOfTask
import com.example.AlertSoon.ui.navigation.FeatureNavScreen
import com.example.AlertSoon.ui.theme.darkTaskThemeColors
import com.example.AlertSoon.ui.theme.lightTaskThemeColors
import com.example.AlertSoon.ui.utils.Constants
import com.example.AlertSoon.ui.utils.Constants.options
import com.example.AlertSoon.ui.utils.DateTime
import com.example.AlertSoon.ui.utils.DateTime.getAmPm
import com.example.AlertSoon.ui.utils.DateTime.getTime



@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewForTaskUiComponentForBlack() {

    val tableOfTask = TableOfTask(
        0,
        "🎯",
        "Hello world",
        "This is the sample hello world description",
        1700133618000,
        0,
        0,
        1700092800000,
        0,
        0,
        0,
        null,
        true,
        "",
        1700133618000
    )
    TaskUIComponent(tableOfTask, false, {}, null)

}

@Composable
fun TaskUIComponent(
    tableOfTask: TableOfTask,
    fromHomeScreen: Boolean = false,
    onDeleteTask: () -> Unit,
    navController: NavHostController? = null,
) {


    Column(
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (fromHomeScreen) Arrangement.SpaceBetween else Arrangement.End
        ) {

            if (fromHomeScreen) {
                Text(
                    text = DateTime.getDateByIterate(tableOfTask.date_in_long!!),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight.W600,
                    overflow = TextOverflow.Ellipsis,
//                    color = taskUI.textColor,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                        )
                        .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                )

            }

            Text(
                text = "⏰ ${getTime(tableOfTask.time_in_long!!)} ${getAmPm(tableOfTask.time_in_long!!)}",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontWeight = FontWeight.W600,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
//                color = taskUI.textColor,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                    )
                    .padding(start = 5.dp, end = 5.dp, top = 5.dp)
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(0.2.dp)
            .background(color = MaterialTheme.colorScheme.outline))



        ConstraintLayout(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .clickable {
                    navController?.navigate(
                        FeatureNavScreen.VIEW_TASK.name
                            .replace("{id}", tableOfTask.uid.toString())
                            .replace("{type}", Constants.options[0])
                    )
                }
                .background(
                    color =  MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp
                    )
                )
                .padding(vertical = 10.dp, horizontal = 6.dp)
        ) {

            val leadIcon = createRef()
            val deleteIcon = createRef()
            val data = createRef()
            val snooze_time = createRef()

            Text(
                text = tableOfTask.leadIcon,
                fontSize = 35.sp,
                modifier = Modifier
                    .constrainAs(leadIcon) {
                        start.linkTo(parent.start)
                        top.linkTo(data.top)
                        bottom.linkTo(data.bottom)
                    }
                    .padding(bottom = 3.dp)
            )


            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .constrainAs(data) {
                        start.linkTo(leadIcon.end)
                        top.linkTo(parent.top)
                        end.linkTo(deleteIcon.start)
                        width = Dimension.fillToConstraints
                    }
            ) {


                Text(
                    text = tableOfTask.task_title ?: "",
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight.W600
                )


                Text(
                    text = tableOfTask.task_description ?: "",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight.W600,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )


            }


            Image(
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = null,

                modifier = Modifier
                    .constrainAs(deleteIcon) {
                        top.linkTo(data.top)
                        bottom.linkTo(data.bottom)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        onDeleteTask()
                    }
                    .width(35.dp)
                    .height(35.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                    .padding(5.dp),
                colorFilter = ColorFilter.tint(color = Color.White)

            )


            if (tableOfTask.snozze_time != null) {
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .constrainAs(snooze_time) {
                            end.linkTo(parent.end)
                            top.linkTo(data.bottom)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = DateTime.getTimeTextForSnooze(tableOfTask.snozze_time!!),
                        fontSize = 13.sp,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),

                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Image(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = R.drawable.baseline_snooze_24),
                        contentDescription = "snooze icon",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }


    }

}

