package com.alertSoon.alarm.ui.component

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
import com.alertSoon.alarm.R
import com.alertSoon.alarm.ui.local_storage.Task.TableOfTask
import com.alertSoon.alarm.ui.navigation.FeatureNavScreen
import com.alertSoon.alarm.ui.theme.dimens

import com.alertSoon.alarm.ui.utils.Constants
import com.alertSoon.alarm.ui.utils.DateTime
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
            .padding(horizontal = MaterialTheme.dimens.card_task_ui_component_horizontal_padding, vertical = MaterialTheme.dimens.card_task_ui_component_vertical_padding)
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

                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
//                color = taskUI.textColor,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(topStart = MaterialTheme.dimens.card_task_ui_component_time_top_start_corner, topEnd = MaterialTheme.dimens.card_task_ui_component_time_top_end_corner)
                    )
                    .padding(start = MaterialTheme.dimens.card_task_ui_component_time_all_padding, end = MaterialTheme.dimens.card_task_ui_component_time_all_padding, top = MaterialTheme.dimens.card_task_ui_component_time_all_padding)
            )

        }


        Box(modifier = Modifier.fillMaxWidth().height(MaterialTheme.dimens.card_task_ui_component_divider_height).background(MaterialTheme.colorScheme.outline))
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
                    RoundedCornerShape(topStart = 0.dp,bottomStart = MaterialTheme.dimens.card_task_ui_component_bottom_start_corner, bottomEnd = MaterialTheme.dimens.card_task_ui_component_bottom_end_corner)
                )

                .padding(vertical = MaterialTheme.dimens.card_task_ui_component_internal_vertical_padding, horizontal = MaterialTheme.dimens.card_task_ui_component_internal_horizontal_padding)
        ) {

            val leadIcon = createRef()
            val deleteIcon = createRef()
            val data = createRef()
            val snooze_time = createRef()

            Text(
                text = tableOfTask.leadIcon,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                modifier = Modifier
                    .constrainAs(leadIcon) {
                        start.linkTo(parent.start)
                        top.linkTo(data.top)
                        bottom.linkTo(data.bottom)
                    }
                    .padding(bottom = MaterialTheme.dimens.card_task_lead_icon_padding)
            )


            Column(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.card_task_data_padding)
                    .constrainAs(data) {
                        start.linkTo(leadIcon.end)
                        top.linkTo(parent.top)
                        end.linkTo(deleteIcon.start)
                        width = Dimension.fillToConstraints
                    }
            ) {


                Text(
                    text = tableOfTask.task_title ?: "",

                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontFamily =  MaterialTheme.typography.titleLarge.fontFamily,
                    fontWeight =  MaterialTheme.typography.titleLarge.fontWeight,
                    modifier = Modifier.padding(start = MaterialTheme.dimens.card_task_text_padding)
                )


                Text(
                    text = tableOfTask.task_description ?: "",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = MaterialTheme.dimens.card_task_text_padding)
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
                    .size(MaterialTheme.dimens.card_task_ui_delete_size)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                    .padding(MaterialTheme.dimens.card_task_ui_delete_padding),
                colorFilter = ColorFilter.tint(color = Color.White)
            )


            if (tableOfTask.snozze_time != null) {
                Row(
                    modifier = Modifier
                        .padding(top = MaterialTheme.dimens.card_task_ui_snozze_time_padding)
                        .constrainAs(snooze_time) {
                            end.linkTo(parent.end)
                            top.linkTo(data.bottom)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = DateTime.getTimeTextForSnooze(tableOfTask.snozze_time!!),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        fontFamily =  MaterialTheme.typography.titleMedium.fontFamily,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.card_task_ui_snozze_time_spacer))

                    Image(
                        modifier = Modifier.size(MaterialTheme.dimens.card_task_ui_snozze_time_icon),
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
                shape = RoundedCornerShape(topStart = MaterialTheme.dimens.card_task_ui_component_time_top_start_corner, topEnd = MaterialTheme.dimens.card_task_ui_component_time_top_end_corner)
            )
            .padding(start = MaterialTheme.dimens.get_all_day_of_regular_task_as_text_start_padding, end = MaterialTheme.dimens.get_all_day_of_regular_task_as_text_end_padding, top =  MaterialTheme.dimens.get_all_day_of_regular_task_as_text_top_padding)
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
        fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.day_in_text),
        fontWeight = if (active) MaterialTheme.typography.titleMedium.fontWeight else FontWeight.Normal
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