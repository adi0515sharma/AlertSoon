package com.example.notifyme.ui.component

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
import com.example.notifyme.R
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
                fontSize = 15.sp,
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.onBackground
            )

        }


        ConstraintLayout(
            modifier = Modifier
                .border(
                    1.dp,
                    if (error != null) Color.Red else Color.Gray,
                    RoundedCornerShape(4.dp)
                )
                .padding(vertical = 10.dp, horizontal = 10.dp)
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

                    fontSize = 18.sp,
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
                        .width(20.dp)
                        .height(20.dp),
                    painter = painterResource(id = if (isPasswordVisible) R.drawable.ic_svg_open_eye else R.drawable.ic_svg_close_eye),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )

            }


        }



        if (error != null) {
            Text(
                text = "* $error",
                fontSize = 10.sp,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontWeight = FontWeight.W400,
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
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = title,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        fontWeight = FontWeight.SemiBold,
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "CANCEL",
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),

                            modifier = Modifier.clickable {
                                setShowDialog(false)
                            },
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = "SAVE",
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),

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
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    LazyColumn{
                        itemsIndexed(options){ index, item ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    val modifiedString = StringBuilder(daysValue)
                                    modifiedString.setCharAt(
                                        index,
                                        if (daysValue[index] == '1') '0' else '1'
                                    )
                                    daysValue = modifiedString.toString()


                                }, verticalAlignment = Alignment.CenterVertically){
                                Checkbox(checked =  daysValue[index] == '1' , onCheckedChange = {

                                    val modifiedString = StringBuilder(daysValue)
                                    modifiedString.setCharAt(index, if(daysValue[index] == '1') '0' else '1')
                                    daysValue = modifiedString.toString()

                                 })
                                Text(text = options[index])
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "CANCEL",
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable {
                                setShowDialog(false)
                            }
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = "SAVE",
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
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
        .padding(vertical = 5.dp, horizontal = 5.dp)
        .clickable { isDialog = !isDialog }
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_repeat_24),
            contentDescription = null,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .padding(7.dp),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Column(
                modifier = Modifier.widthIn(min = 32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Days",
                    fontWeight = FontWeight.W600,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 15.sp,
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
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline) // Customize the color of the underline
                )
            }

            Spacer(modifier = Modifier.height(2.dp))


            if (!value.contains("1")) {
                Text(
                    text = "Select day's here",
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 15.sp,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            } else {
                getAllDayOfRegularTask(value)
            }

            Spacer(modifier = Modifier.height(2.dp))
            if (error != null) {
                Text(
                    text = "* $error",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight.W400,
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
        fontFamily = FontFamily(Font(R.font.poppins_regular)),

        modifier = Modifier
            .padding(2.dp)
            .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(10.dp))
            .padding(5.dp),
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
        .padding(vertical = 5.dp, horizontal = 5.dp)
        .then(
            if (shouldOpenDialog) Modifier.clickable { isDialog = !isDialog }
            else Modifier
        )

    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .width(40.dp)
                .height(40.dp)
                .padding(7.dp),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Column(
                modifier = Modifier.widthIn(min = 32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.W600,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 15.sp,
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
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.onBackground) // Customize the color of the underline
                )
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (value.isNullOrEmpty()) hint?:"" else value,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                color = if (value.isNullOrEmpty()) Color.Gray else MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            if (error != null) {
                Text(
                    text = "* $error",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight.W400,
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