package com.example.AlertSoon.ui.screens.home_screen_activity.ui.composable_screen.home_screen.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.AlertSoon.R
import com.example.AlertSoon.ui.component.AppBar
import com.example.AlertSoon.ui.component.CollapsingLayout
import com.example.AlertSoon.ui.component.ISSUES
import com.example.AlertSoon.ui.component.IssueCardUiComponent
import com.example.AlertSoon.ui.component.IssueDataClass
import com.example.AlertSoon.ui.component.IssueExecution
import com.example.AlertSoon.ui.component.IssueHandler
import com.example.AlertSoon.ui.component.RegularTaskUiComponent
import com.example.AlertSoon.ui.component.TaskUIComponent
import com.example.AlertSoon.ui.local_storage.Task.TableOfTask
import com.example.AlertSoon.ui.navigation.FeatureNavScreen
import com.example.AlertSoon.ui.screens.home_screen_activity.ui.HomeActivityViewModel
import com.example.AlertSoon.ui.utils.Constants.options
import com.example.AlertSoon.ui.utils.DateTime.getDateByIterate
import com.example.notifyme.ui.component.LoaderSection
import com.example.notifyme.ui.component.NoTaskAvailableUi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenComposable(
    navController: NavHostController,
    viewModel: HomeActivityViewModel,
    onDeleteTask: (parameter: Long, type: String) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = 2)

    Scaffold(
        topBar = {
            AppBar(navController)
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->

            Column(modifier = Modifier.padding(padding)) {

                Column(modifier = Modifier.fillMaxWidth()) {
//                        IssueSection(issueExecution,viewModel)
                    NextFiveTaskSection(viewModel, navController, onDeleteTask)
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outline)
                    )
                    TaskTabs(pagerState = pagerState)
                    // on below line we are calling tabs content
                    // for displaying our page for each tab layout
                    TaskTabsContent(
                        navController = navController,
                        pagerState = pagerState,
                        viewmodel = viewModel,
                        onDeleteTask = onDeleteTask
                    )
                }

            }


        },
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun IssueSection(issueExecution: IssueExecution, viewModel: HomeActivityViewModel) {
    var issues by rememberSaveable { mutableStateOf(listOf<IssueDataClass>()) }

    var pagerState = rememberPagerState(pageCount = issues.size)


    val context = LocalContext.current

    LaunchedEffect(key1 = null) {

        if (issues.isEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                issues += mutableListOf(
                    IssueDataClass(
                        "Notification permission is not granted",
                        ISSUES.NO_NOTIFICATION_ALLOWED
                    )
                )
            }



            if (!(context.getSystemService(ComponentActivity.POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(
                    context.packageName
                )
            ) {
                issues += mutableListOf(
                    IssueDataClass(
                        "please allow battery consumption in background",
                        ISSUES.BATTERY_CONSUMPTION_REQUIRED
                    )
                )
            }
        }


    }

    if (issues.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {


            Text(text = "There is ${issues.size} Issue, Which affect in notification")
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalPager(state = pagerState) {
                    IssueCardUiComponent(issueDataClass = issues[it], object : IssueHandler {
                        override fun closeIssue() {

                            issues = issues.toMutableList().also { i ->
                                i.remove(issues[it])
                            } // remove

                        }

                        override fun performIssue() {

                            when (issues[it].type) {
                                ISSUES.BATTERY_CONSUMPTION_REQUIRED -> {
                                    issueExecution.performBatteryConsumption()
                                }

                                ISSUES.NO_NOTIFICATION_ALLOWED -> {
                                    issueExecution.performNotificationAllowence()

                                }

                                else -> {
                                    issueExecution.performSomeSettingTask()
                                }
                            }
                        }

                    })
                }
            }


        }
    }


}


@OptIn(ExperimentalPagerApi::class)
@ExperimentalPagerApi
@Composable
fun TaskTabs(pagerState: PagerState) {
    val list = listOf("Once", "Regular")
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colorScheme.background,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        Tab(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.baseline_date_range_24),
                    contentDescription = "once icon",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
            },
            text = {
                Text(
                    list[0],
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            selected = pagerState.currentPage == 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
        )

        Tab(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.baseline_repeat_24),
                    contentDescription = "regular icon",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
            },
            text = {

                Text(
                    list[1],
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            selected = pagerState.currentPage == 1,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }
        )
    }
}

@ExperimentalPagerApi
@Composable
fun TaskTabsContent(
    navController: NavHostController,
    pagerState: PagerState,
    viewmodel: HomeActivityViewModel,
    onDeleteTask: (parameter: Long, type: String) -> Unit,

    ) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> OnceTaskSection(
                viewmodel,
                navController,
                onDeleteTask
            )

            1 -> RegularTaskSection(
                viewmodel,
                navController,
                onDeleteTask
            )
        }
    }
}

@Composable
fun OnceTaskSection(
    viewmodel: HomeActivityViewModel,
    navController: NavHostController,
    onDeleteTask: (parameter: Long, type: String) -> Unit
) {


    var once_task by rememberSaveable { mutableStateOf<MutableList<TableOfTask>>(mutableListOf()) }
    var isLoaderVisible by rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = null) {

        viewmodel.once_tasks.collectLatest {
            once_task = it
            isLoaderVisible = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (isLoaderVisible) {
            LoaderSection()
        } else {
            if (once_task.isEmpty()) {
                NoTaskAvailableUi()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                    val groupedItems = once_task.groupBy { it.date_in_long }
                    groupedItems.forEach {

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = getDateByIterate(it.key!!),
                                    modifier = Modifier
                                        .padding(
                                            top = if (groupedItems.entries.indexOf(it) != 0) 20.dp else 8.dp,
                                            bottom = 5.dp
                                        )
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }


                        itemsIndexed(it.value) { index, t ->


                            TaskUIComponent(

                                tableOfTask = t,
                                false,
                                {
                                    t.uid ?: return@TaskUIComponent
                                    onDeleteTask(t.uid, options[0])
                                },
                                navController
                            )

                        }
                    }


                }
            }

        }

    }
}

@ExperimentalPagerApi
@Composable
fun NextFiveTaskSection(
    viewmodel: HomeActivityViewModel,
    navController: NavHostController,
    onDeleteTask: (parameter: Long, type: String) -> Unit
) {


    var nextFiveTasks by rememberSaveable { mutableStateOf<MutableList<TableOfTask>>(mutableListOf()) }
    var pageCount by rememberSaveable { mutableStateOf(0) }
    var pagerState = rememberPagerState(pageCount = pageCount)
    var isLoaderVisible by rememberSaveable {
        mutableStateOf(true)
    }
    val createYourTaskColor = MaterialTheme.colorScheme.primary


    LaunchedEffect(key1 = null) {
        viewmodel.next_five_tasks.collectLatest {
            nextFiveTasks = it
            pageCount = it.size
            isLoaderVisible = false
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {

        if (isLoaderVisible) {
            LoaderSection()
        } else {
            if (nextFiveTasks.isNotEmpty()) {
                Text(text = "Your Next ${nextFiveTasks.size} Task")
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalPager(state = pagerState) {


                        if (nextFiveTasks!![it].is_regular) {

                            val allDayTask = nextFiveTasks!![it].days
                            RegularTaskUiComponent(Modifier, nextFiveTasks!![it], allDayTask, {
                                nextFiveTasks!![it].uid ?: return@RegularTaskUiComponent
                                onDeleteTask(nextFiveTasks!![it].uid!!, options[1])
                            }, navController)

                        } else {

                            TaskUIComponent(
                                nextFiveTasks!![it], true, {
                                    nextFiveTasks!![it].uid ?: return@TaskUIComponent
                                    onDeleteTask(nextFiveTasks!![it].uid!!, options[0])
                                },
                                navController
                            )

                        }
                    }
                }

            } else {
                Column(
                    modifier = Modifier
                        .clickable {
                            navController.navigate(FeatureNavScreen.CREATING_TASK.name)
                        }
                        .fillMaxWidth()
                        .drawBehind {
                            drawRoundRect(
                                color = createYourTaskColor, style = Stroke(
                                    width = 5f,
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(10f, 10f),
                                        0f
                                    )
                                )
                            )
                        }
                        .padding(horizontal = 10.dp, vertical = 35.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_add_task_24),
                            contentDescription = "add task",
                            colorFilter = ColorFilter.tint(color = createYourTaskColor)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Create Your Task's",
                            fontFamily = FontFamily(Font(R.font.poppins_regular)),
                            fontWeight = FontWeight.W600,
                            color = createYourTaskColor
                        )
                    }
                }
            }

        }

    }
}


@Composable
fun RegularTaskSection(
    viewmodel: HomeActivityViewModel,
    navController: NavHostController,
    onDeleteTask: (parameter: Long, type: String) -> Unit
) {

    var regular_tasks by rememberSaveable { mutableStateOf<MutableList<TableOfTask>>(mutableListOf()) }
    var isLoaderVisible by rememberSaveable {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = null) {
        viewmodel.regular_tasks.collectLatest {
            regular_tasks = it
            isLoaderVisible = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {


        if (isLoaderVisible) {
            LoaderSection()
        } else {
            if (regular_tasks.isEmpty()) {
                NoTaskAvailableUi()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                ) {
                    itemsIndexed(regular_tasks!!) { value, it ->
                        val allDayTask = it.days
                        RegularTaskUiComponent(Modifier, it, allDayTask, {
                            it.uid ?: return@RegularTaskUiComponent
                            onDeleteTask(it.uid!!, options[1])
                        }, navController)
                    }
                }
            }

        }


    }
}










