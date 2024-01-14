package com.alertSoon.alarm.ui.screens.home_screen_activity.ui.composable_screen.home_screen.ui

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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.alertSoon.alarm.R
import com.alertSoon.alarm.ui.component.AppBar
import com.alertSoon.alarm.ui.component.RegularTaskUiComponent
import com.alertSoon.alarm.ui.component.TaskUIComponent
import com.alertSoon.alarm.ui.local_storage.Task.TableOfTask
import com.alertSoon.alarm.ui.navigation.FeatureNavScreen
import com.alertSoon.alarm.ui.screens.home_screen_activity.ui.HomeActivityViewModel
import com.alertSoon.alarm.ui.theme.dimens
import com.alertSoon.alarm.ui.utils.DateTime.getDateByIterate
import com.alertSoon.alarm.ui.component.LoaderSection
import com.alertSoon.alarm.ui.component.NoTaskAvailableUi
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
) {
    val pagerState = rememberPagerState(pageCount = 2)

    var deleteCoroutineScope = rememberCoroutineScope()
    var taskIdToDelete by rememberSaveable {
        mutableStateOf<Long?>(null)
    }
    Scaffold(
        topBar = {
            AppBar(navController)
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->

            Column(modifier = Modifier.padding(padding)) {

                Column(modifier = Modifier.fillMaxWidth()) {
                    NextFiveTaskSection(viewModel, navController)
                    { parameter ->
                        taskIdToDelete = parameter
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.dimens.home_screen_composable_outline_height)
                            .background(MaterialTheme.colorScheme.outline)
                    )
                    TaskTabs(pagerState = pagerState)
                    TaskTabsContent(
                        navController = navController,
                        pagerState = pagerState,
                        viewmodel = viewModel,
                    ){ parameter ->
                        taskIdToDelete = parameter
                    }
                }

            }

            if(taskIdToDelete!=null){
                DeleteDialog{
                    if(it){
                        deleteCoroutineScope.launch {
                            viewModel.deleteTask(taskIdToDelete!!)
                            taskIdToDelete = null
                        }
                    }
                    else{
                        taskIdToDelete = null
                    }
                }
            }

        },
    )
}
@Composable
fun DeleteDialog(
    setShowDialog: (Boolean) -> Unit,
) {



    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(MaterialTheme.dimens.delete_dialog_rounded_corner_shape),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(MaterialTheme.dimens.delete_dialog_padding)) {
                    Text(
                        text = "Delete Task",
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize,

                        color = MaterialTheme.colorScheme.onBackground
                    )


                    Text(
                        text = "Do you want to delete this task ?",
                        fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                        fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.delete_dialog_spacer))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "No",
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            modifier = Modifier.clickable {
                                setShowDialog(false)
                            },
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.delete_dialog_action_spacer))
                        Text(
                            text = "Yes",
                            fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            modifier = Modifier.clickable {
                                setShowDialog(true)
                            },
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
//
//@OptIn(ExperimentalPagerApi::class)
//@Composable
//fun IssueSection(issueExecution: IssueExecution, viewModel: HomeActivityViewModel) {
//    var issues by rememberSaveable { mutableStateOf(listOf<IssueDataClass>()) }
//
//    var pagerState = rememberPagerState(pageCount = issues.size)
//
//
//    val context = LocalContext.current
//
//    LaunchedEffect(key1 = null) {
//
//        if (issues.isEmpty()) {
//            if (ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//
//                issues += mutableListOf(
//                    IssueDataClass(
//                        "Notification permission is not granted",
//                        ISSUES.NO_NOTIFICATION_ALLOWED
//                    )
//                )
//            }
//
//
//
//            if (!(context.getSystemService(ComponentActivity.POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(
//                    context.packageName
//                )
//            ) {
//                issues += mutableListOf(
//                    IssueDataClass(
//                        "please allow battery consumption in background",
//                        ISSUES.BATTERY_CONSUMPTION_REQUIRED
//                    )
//                )
//            }
//        }
//
//
//    }
//
//    if (issues.isNotEmpty()) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp)
//        ) {
//
//
//            Text(text = "There is ${issues.size} Issue, Which affect in notification")
//            Spacer(modifier = Modifier.height(8.dp))
//            Column(modifier = Modifier.fillMaxWidth()) {
//                HorizontalPager(state = pagerState) {
//                    IssueCardUiComponent(issueDataClass = issues[it], object : IssueHandler {
//                        override fun closeIssue() {
//
//                            issues = issues.toMutableList().also { i ->
//                                i.remove(issues[it])
//                            } // remove
//
//                        }
//
//                        override fun performIssue() {
//
//                            when (issues[it].type) {
//                                ISSUES.BATTERY_CONSUMPTION_REQUIRED -> {
//                                    issueExecution.performBatteryConsumption()
//                                }
//
//                                ISSUES.NO_NOTIFICATION_ALLOWED -> {
//                                    issueExecution.performNotificationAllowence()
//
//                                }
//
//                                else -> {
//                                    issueExecution.performSomeSettingTask()
//                                }
//                            }
//                        }
//
//                    })
//                }
//            }
//
//
//        }
//    }
//
//
//}


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
                height = MaterialTheme.dimens.tab_indicator_height,
                color = MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier.padding(vertical = MaterialTheme.dimens.tabbar_vertical_padding)
    ) {
        Tab(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.baseline_date_range_24),
                    contentDescription = "once icon",
                    modifier = Modifier
                        .size(MaterialTheme.dimens.home_screen_regular_and_once_icon_size),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
            },
            text = {
                Text(
                    list[0],
                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
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
                    modifier = Modifier
                        .size(MaterialTheme.dimens.home_screen_regular_and_once_icon_size),

                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
            },
            text = {

                Text(
                    list[1],
                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
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
    onDeleteTask: (parameter: Long) -> Unit,

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
    onDeleteTask: (parameter: Long) -> Unit
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
                                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
                                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                    fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
                                    modifier = Modifier
                                        .padding(
                                            top = if (groupedItems.entries.indexOf(it) != 0) MaterialTheme.dimens.once_task_date_item_top_padding_other_item else MaterialTheme.dimens.once_task_date_item_top_padding_first_item,
                                            bottom = MaterialTheme.dimens.once_task_date_item_bottom_padding
                                        )
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(MaterialTheme.dimens.once_task_date_item_rounded_corner_shape)
                                        )
                                        .padding(horizontal = MaterialTheme.dimens.once_task_date_item_inside_horizontal_padding, vertical = MaterialTheme.dimens.once_task_date_item_inside_vertical_padding)
                                )
                            }
                        }


                        itemsIndexed(it.value) { index, t ->


                            TaskUIComponent(

                                tableOfTask = t,
                                false,
                                {
                                    t.uid ?: return@TaskUIComponent
                                    onDeleteTask(t.uid)
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
    onDeleteTask: (parameter: Long) -> Unit
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
            .padding(MaterialTheme.dimens.next_five_task_section_padding)
    ) {

        if (isLoaderVisible) {
            LoaderSection()
        } else {
            if (nextFiveTasks.isNotEmpty()) {
                Text(
                    text = "Your Next ${nextFiveTasks.size} Task",
                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.next_five_task_section_spacer))
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalPager(state = pagerState) {


                        if (nextFiveTasks[it].is_regular) {

                            val allDayTask = nextFiveTasks[it].days
                            RegularTaskUiComponent(Modifier, nextFiveTasks[it], allDayTask, {
                                nextFiveTasks[it].uid ?: return@RegularTaskUiComponent
                                onDeleteTask(nextFiveTasks[it].uid!!)
                            }, navController)

                        } else {

                            TaskUIComponent(
                                nextFiveTasks[it], true, {
                                    nextFiveTasks[it].uid ?: return@TaskUIComponent
                                    onDeleteTask(nextFiveTasks[it].uid!!)
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
                        .padding(horizontal = MaterialTheme.dimens.next_five_task_section_create_task_horizontal_padding, vertical = MaterialTheme.dimens.next_five_task_section_create_task_vertical_padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_add_task_24),
                            contentDescription = "add task",
                            modifier = Modifier
                                .size(MaterialTheme.dimens.home_screen_create_your_task_icon_size),
                            colorFilter = ColorFilter.tint(color = createYourTaskColor)
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.dimens.next_five_task_section_create_task_between_padding))
                        Text(
                            text = "Create Your Task's",
                            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                            fontSize = MaterialTheme.typography.labelMedium.fontSize,
                            fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
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
    onDeleteTask: (parameter: Long) -> Unit
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
                        .padding(top = MaterialTheme.dimens.regular_task_section_padding)
                ) {
                    itemsIndexed(regular_tasks) { value, it ->
                        val allDayTask = it.days
                        RegularTaskUiComponent(Modifier, it, allDayTask, {
                            it.uid ?: return@RegularTaskUiComponent
                            onDeleteTask(it.uid)
                        }, navController)
                    }
                }
            }

        }


    }
}










