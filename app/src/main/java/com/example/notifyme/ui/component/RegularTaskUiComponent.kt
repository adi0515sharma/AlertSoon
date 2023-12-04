package com.example.notifyme.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.notifyme.R
import com.example.notifyme.ui.local_storage.Task.TableOfTask
import com.example.notifyme.ui.navigation.FeatureNavScreen
import com.example.notifyme.ui.theme.darkTaskThemeColors
import com.example.notifyme.ui.theme.lightTaskThemeColors
import com.example.notifyme.ui.utils.Constants
import com.example.notifyme.ui.utils.DateTime
import java.util.Calendar


@Composable
fun RegularTaskUiComponent(
    modifier: Modifier,
    tableOfTask: TableOfTask,
    allDayTask: String,
    onDeleteTask: () -> Unit,
    navController: NavHostController? = null
) {


    Column(
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.End

        ) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            getAllDayOfRegularTaskAsText(allDayTask)


            Text(
                text = "⏰ ${DateTime.getTime(tableOfTask.time_in_long!!)} ${
                    DateTime.getAmPm(
                        tableOfTask.time_in_long!!
                    )
                }",
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


        Box(modifier = Modifier.fillMaxWidth().height(0.2.dp).background(MaterialTheme.colorScheme.outline))
        ConstraintLayout(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .clickable {
                    navController?.navigate(
                        FeatureNavScreen.VIEW_TASK.name
                            .replace("{id}", tableOfTask.uid.toString())
                            .replace("{type}", Constants.options[1])
                    )
                }
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(topStart = 0.dp,bottomStart = 10.dp, bottomEnd = 10.dp)
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
                    fontWeight = FontWeight.W600,
                    modifier = Modifier.padding(start = 2.dp)
                )


                Text(
                    text = tableOfTask.task_description ?: "",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight.W600,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 2.dp)
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


@Composable
fun getAllDayOfRegularTaskAsText(allDayTask: String) {


    Row(

        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
            )
            .padding(start = 5.dp, end = 5.dp, top = 5.dp)
    ) {


        dayInText("S", allDayTask[0] == '1')

        dayInText("M", allDayTask[1] == '1')

        dayInText("T", allDayTask[2] == '1')

        dayInText("W", allDayTask[3] == '1')

        dayInText("T", allDayTask[4] == '1')

        dayInText("F", allDayTask[5] == '1')

        dayInText("S", allDayTask[6] == '1')

    }
}

@Composable
fun dayInText(txt: String, active: Boolean) {


    Text(
        text = txt,
        color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary,
        fontFamily = FontFamily(Font(R.font.poppins_regular)),
        modifier = Modifier.padding(horizontal = 2.dp),
        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
    )
}


@Preview
@Composable
fun PreviewRegularTaskUiComponentForWhite() {

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
    RegularTaskUiComponent(Modifier, tableOfTask, "0001000", {}, null)

}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewRegularTaskUiComponentForBlack() {

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
    RegularTaskUiComponent(Modifier, tableOfTask, "0001000", {}, null)

}