package com.example.AlertSoon.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.AlertSoon.R
import com.example.AlertSoon.ui.theme.dimens
import java.util.Calendar

@Composable
fun EntryField(
    modifier: Modifier,
    value: String,
    setEntryFieldValue: SetEntryFieldValue?,
    hint: String? = null,
    error: String?,
    isPasswordFeatureEnable: Boolean = false,
    isSingleLine: Boolean = true
) {

    var isPasswordVisible by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)

    ) {
        if (hint != null) {
            Text(
                text = hint,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                color = MaterialTheme.colorScheme.onBackground
            )

        }


        ConstraintLayout(
            modifier = Modifier
                .border(
                    MaterialTheme.dimens.input_field_border_width,
                    if (error != null) Color.Red else Color.Gray,
                    RoundedCornerShape(MaterialTheme.dimens.input_field_rounded_corner_shape)
                )
                .padding(MaterialTheme.dimens.input_field_all_padding)
                .fillMaxWidth()
        ) {

            var (inputfield, password) = createRefs()

            BasicTextField(
                value = value,
                visualTransformation =
                if (isPasswordFeatureEnable) {
                    if (isPasswordVisible) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    }
                } else {
                    VisualTransformation.None
                },

                modifier = modifier
                    .constrainAs(inputfield) {
                        start.linkTo(parent.start)
                        if (isPasswordFeatureEnable) {
                            end.linkTo(password.start)
                        } else {
                            end.linkTo(parent.end)
                        }
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    },
                onValueChange = { setEntryFieldValue?.setValue(it) },
                textStyle = TextStyle(

                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = MaterialTheme.colorScheme.onBackground,// Center text horizontally

                ),
                singleLine = isSingleLine,
            )
            if (isPasswordFeatureEnable) {
                Image(
                    modifier = Modifier
                        .clickable {
                            isPasswordVisible = !isPasswordVisible
                        }
                        .constrainAs(password) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(MaterialTheme.dimens.password_protect_icon_size),
                    painter = painterResource(id = if (isPasswordVisible) R.drawable.ic_svg_open_eye else R.drawable.ic_svg_close_eye),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )

            }


        }



        if (error != null) {
            Text(
                text = "* $error",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

}


@Composable
fun CustomDialog(
    title: String,
    value: String,
    hint: String? = null,
    setShowDialog: (Boolean) -> Unit,
    setValue: (String) -> Unit
) {

    var txtField by rememberSaveable { mutableStateOf(value) }
    var hintField by rememberSaveable { mutableStateOf(hint) }


    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(MaterialTheme.dimens.dialog_rounded_corner_shape),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(MaterialTheme.dimens.dialog_all_padding)) {
                    Text(
                        text = title,
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize,

                        color = MaterialTheme.colorScheme.onBackground
                    )

                    EntryField(
                        Modifier,
                        txtField,
                        object : SetEntryFieldValue {
                            override fun setValue(s: String) {
                                txtField = s
                            }
                        },
                        hintField,
                        null,
                        false,
                        true
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.dialog_spacing))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "CANCEL",
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            modifier = Modifier.clickable {
                                setShowDialog(false)
                            },
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.dialog_action_button_spacing))
                        Text(
                            text = "SAVE",
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            modifier = Modifier.clickable {
                                setValue(txtField)
                                setShowDialog(false)
                            },
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DialogOfDays(
    value: String,
    setShowDialog: (Boolean) -> Unit,
    setValue: (String) -> Unit
) {

    val options = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    var daysValue by rememberSaveable { mutableStateOf(value) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(MaterialTheme.dimens.dialog_rounded_corner_shape),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(MaterialTheme.dimens.dialog_all_padding)) {

                    LazyColumn{
                        itemsIndexed(options){ index, item ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.dimens.checkbox_vertical_padding)
                                .clickable {

                                    val modifiedString = StringBuilder(daysValue)
                                    modifiedString.setCharAt(
                                        index,
                                        if (daysValue[index] == '1') '0' else '1'
                                    )
                                    daysValue = modifiedString.toString()


                                },
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Checkbox(
                                    checked =  daysValue[index] == '1' ,
                                    onCheckedChange = {

                                    val modifiedString = StringBuilder(daysValue)
                                    modifiedString.setCharAt(index, if(daysValue[index] == '1') '0' else '1')
                                    daysValue = modifiedString.toString()

                                 }
                                )
                                Text(text = options[index],
                                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                    fontWeight = MaterialTheme.typography.labelSmall.fontWeight,)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "CANCEL",
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                setShowDialog(false)
                            }
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.dialog_action_button_spacing))
                        Text(
                            text = "SAVE",
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                setValue(daysValue)
                                setShowDialog(false)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EntryFieldWithDialogForSelectingDays(
    modifier: Modifier,
    error: String? = null,
    value: String,
    setEntryFieldValueForDay: SetEntryFieldValueForDay? = null
) {
    var isDialog by rememberSaveable { mutableStateOf(false) }

    Row(modifier = modifier
        .padding(MaterialTheme.dimens.entry_field_with_dialog_for_selecting_days_all_padding)
        .clickable { isDialog = !isDialog }
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_repeat_24),
            contentDescription = null,
            modifier = Modifier
                .size(MaterialTheme.dimens.entry_field_with_dialog_for_selecting_days_icon_size)
                .padding(MaterialTheme.dimens.entry_field_with_dialog_for_selecting_days_icon_all_padding),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.entry_field_with_dialog_for_selecting_days_horizontal_padding)
        ) {
            Column(
                modifier = Modifier.widthIn(min = MaterialTheme.dimens.entry_field_with_dialog_for_selecting_days_minimum_width),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Days",
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    modifier = Modifier.wrapContentSize(),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(MaterialTheme.dimens.entry_field_with_dialog_for_selecting_days_divider)
                        .background(MaterialTheme.colorScheme.outline) // Customize the color of the underline
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.entry_field_with_dialog_for_selecting_days_spacer))


            if (!value.contains("1")) {
                Text(
                    text = "Select day's here",
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    fontSize =  MaterialTheme.typography.bodySmall.fontSize,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            } else {
                getAllDayOfRegularTask(value)
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.entry_field_with_dialog_for_selecting_days_spacer))
            if (error != null) {
                Text(
                    text = "* $error",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                )
            }
        }
    }

    if (isDialog) {
        DialogOfDays(value, { isDialog = it }, { setEntryFieldValueForDay?.setValue(it)})
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun getAllDayOfRegularTask(allDayTask : String) {


    if(!allDayTask.contains("0")){
        dayInChip("Every Day")
    }
    else {
        FlowRow(
            modifier = Modifier.fillMaxWidth()
        ) {

            if (allDayTask[0] == '1')
                dayInChip("Sunday")
            if (allDayTask[1] == '1')
                dayInChip("Monday")
            if (allDayTask[2] == '1')
                dayInChip("Tuesday")
            if (allDayTask[3] == '1')
                dayInChip("Wednesday")
            if (allDayTask[4] == '1')
                dayInChip("Thursday")
            if (allDayTask[5] == '1')
                dayInChip("Friday")
            if (allDayTask[6] == '1')
                dayInChip("Saturday")

        }
    }

}

@Composable
fun dayInChip(txt: String) {
    Text(
        text = txt,
        fontFamily = MaterialTheme.typography.titleMedium.fontFamily,

        modifier = Modifier
            .padding(MaterialTheme.dimens.day_in_chip_external_padding)
            .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(MaterialTheme.dimens.day_in_chip_corner_curve))
            .padding(MaterialTheme.dimens.day_in_chip_internal_padding),
    )
}

@Composable
fun EntryFieldWithDialog(
    modifier: Modifier,
    icon: Int,
    title: String,
    hint: String? = null,
    error: String? = null,
    value: String? = null,
    setEntryFieldValue: SetEntryFieldValue? = null,
    shouldOpenDialog: Boolean = true
) {

    var isDialog by rememberSaveable { mutableStateOf(false) }

    Row(modifier = modifier
        .padding(MaterialTheme.dimens.entry_field_with_dialog_padding)
        .then(
            if (shouldOpenDialog) Modifier.clickable { isDialog = !isDialog }
            else Modifier
        )

    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(MaterialTheme.dimens.entry_field_with_dialog_icon_size)
                .padding(MaterialTheme.dimens.entry_field_with_dialog_icon_padding),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.entry_field_with_dialog_data_padding)
        ) {
            Column(
                modifier = Modifier.widthIn(min = MaterialTheme.dimens.entry_field_with_dialog_data_width),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                    fontFamily =  MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize =  MaterialTheme.typography.labelMedium.fontSize,
                    modifier = Modifier.wrapContentSize(),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(MaterialTheme.dimens.entry_field_with_dialog_data_divider)
                        .background(MaterialTheme.colorScheme.onBackground) // Customize the color of the underline
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.entry_field_with_dialog_spacer))
            Text(
                text = if (value.isNullOrEmpty()) hint?:"" else value,
                fontFamily =  MaterialTheme.typography.bodySmall.fontFamily,
                color = if (value.isNullOrEmpty()) Color.Gray else MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.entry_field_with_dialog_spacer))
            if (error != null) {
                Text(
                    text = "* $error",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                    fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                )
            }
        }
    }

    if (isDialog && shouldOpenDialog) {
        CustomDialog(
            title,
            value ?: "",
            null,
            { isDialog = it },
            { setEntryFieldValue?.setValue(it) })
    }

}

@Preview(showSystemUi = true)
@Composable
fun preview() {
    EntryFieldWithDialog(
        Modifier,

        R.drawable.baseline_title_24,
        "Enter the title",
        "No title provided"
    )
}


public interface SetEntryFieldValue {
    public fun setValue(s:String)
}

public interface SetEntryFieldValueForCalender {
    public fun setValue(s: Calendar)
}

public interface SetEntryFieldValueForDay {
    public fun setValue(days : String)
}

public interface DateFieldValue {
    public fun setValue(s: String, long: Long)
}

public interface TimeFieldValue {
    public fun setValue(s: String, long: Long)
}

public interface SelectedDateListener {
    public fun setValue(day : Int, month : Int, year : Int)
}


public interface SelectedTimeListener {
    public fun setValue(hour : Int, minute : Int)
}