package com.example.notifyme.ui.component

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.notifyme.R
import com.example.notifyme.ui.navigation.FeatureNavScreen
import com.example.notifyme.ui.utils.Constants
import com.example.notifyme.ui.utils.Validator.isEmailValid
import com.example.notifyme.ui.utils.Validator.isFieldCorrect
import com.example.notifyme.ui.utils.ValidatorResponse
import kotlinx.android.parcel.Parcelize

@Composable
fun IssueCardUiComponent(issueDataClass: IssueDataClass, issueHandler: IssueHandler) {

    ConstraintLayout(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.errorContainer,
                RoundedCornerShape(
                    topStart = 10.dp,
                    bottomStart = 10.dp,
                    bottomEnd = 10.dp,
                    topEnd = 10.dp
                )
            )
            .padding(vertical = 10.dp, horizontal = 6.dp)
    ){

        val message = createRef()
        val cancelBtn = createRef()
        val letsDoItBtn = createRef()

        Text(
            text = issueDataClass.message,
            color = MaterialTheme.colorScheme.error,
            fontSize = 15.sp,
            textAlign = TextAlign.Start,
            fontFamily = FontFamily(Font(R.font.poppins_regular)),
            modifier = Modifier.constrainAs(message){
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }.padding(bottom = 10.dp)
        )

        AppButton(Modifier.wrapContentWidth().constrainAs(letsDoItBtn){
            end.linkTo(parent.end)
            top.linkTo(message.bottom)
        },"Let's do it", object : HandleClick {
            override fun onClick() {
                issueHandler.performIssue()
            }
        }, true)

        AppButton(Modifier.wrapContentWidth().padding(end = 10.dp).constrainAs(cancelBtn){
            end.linkTo(letsDoItBtn.start)
            top.linkTo(message.bottom)
        },"Cancel", object : HandleClick {
            override fun onClick() {
                issueHandler.closeIssue()
            }
        },true)

    }
}

@Parcelize
data class IssueDataClass(val message : String, val type : ISSUES) : Parcelable

enum class ISSUES{
    NO_NOTIFICATION_ALLOWED,
    SOME_SETTING,
    BATTERY_CONSUMPTION_REQUIRED
}

interface IssueHandler{
    fun closeIssue()
    fun performIssue()
}

interface IssueExecution{
    fun performSomeSettingTask()
    fun performNotificationAllowence()
    fun performBatteryConsumption()
}
