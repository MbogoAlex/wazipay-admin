package com.escrow.wazipay_admin.ui.screen.home

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.escrow.wazipay.utils.formatIsoDateTime2
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import com.escrow.wazipay_admin.AppViewModelFactory
import com.escrow.wazipay_admin.R
import com.escrow.wazipay_admin.data.network.models.user.UserVerificationData
import com.escrow.wazipay_admin.data.network.models.user.VerificationStatus
import com.escrow.wazipay_admin.data.network.models.user.userVerifications
import com.escrow.wazipay_admin.ui.theme.WazipayadminTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UsersScreenComposable(
    navigateToLoginScreenWithArgs: (phoneNumber: String, pin: String) -> Unit,
    navigateToUserDetailsScreen: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel: UsersViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    if(uiState.loadingStatus == LoadingStatus.FAIL) {
        if(uiState.unauthorized) {
            val phone = uiState.userDetails.phoneNumber!!
            val pin = uiState.userDetails.pin!!
            navigateToLoginScreenWithArgs(phone, pin)
        }
        viewModel.resetStatus()
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.loadingStatus == LoadingStatus.LOADING,
        onRefresh = {
            viewModel.initializeData()
        }
    )

    var filtering by rememberSaveable {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        UsersScreen(
            adminUsername = uiState.userDetails.username ?: "",
            pullRefreshState = pullRefreshState,
            totalPages = uiState.totalPages,
            currentPage = uiState.currentPage,
            currentCount = uiState.currentCount,
            totalCount = uiState.totalCount,
            startDate = LocalDate.parse(uiState.startDate),
            endDate = LocalDate.parse(uiState.endDate),
            onChangeStartDate = viewModel::changeStartDate,
            onChangeEndDate = viewModel::changeEndDate,
            filtering = filtering,
            onFilter = {
                filtering = !filtering
            },
            onClearSearch = {},
            onChangeSearchText = viewModel::changeSearchText,
            searchText = uiState.searchText,
            onChangeStatus = viewModel::changeStatus,
            onChangePage = viewModel::changePage,
            loadingStatus = uiState.loadingStatus,
            navigateToUserDetailsScreen = navigateToUserDetailsScreen,
            users = uiState.users
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UsersScreen(
    adminUsername: String,
    pullRefreshState: PullRefreshState?,
    totalPages: Int,
    currentPage: Int,
    currentCount: Int,
    totalCount: Int,
    searchText: String,
    startDate: LocalDate,
    endDate: LocalDate,
    users: List<UserVerificationData>,
    onFilter: () -> Unit,
    filtering: Boolean,
    onClearSearch: () -> Unit,
    onChangeSearchText: (value: String) -> Unit,
    onChangeStartDate: (date: LocalDate) -> Unit,
    onChangeEndDate: (date: LocalDate) -> Unit,
    onChangeStatus: (status: String) -> Unit,
    onChangePage: (page: Int) -> Unit,
    navigateToUserDetailsScreen: (userId: String) -> Unit,
    loadingStatus: LoadingStatus,
    modifier: Modifier = Modifier
) {
    val stages = listOf("All", "Unverified", "Pending verification", "Verified")
    var selectedStage by rememberSaveable {
        mutableStateOf("All")
    }
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = screenWidth(x = 16.0),
                vertical = screenHeight(x = 16.0)
            )
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.account_box),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = "ADMIN:",
                        fontWeight = FontWeight.Bold,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                    Text(
                        text = adminUsername,
                        fontWeight = FontWeight.Bold,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Users",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column {
                        Box(
                            modifier = Modifier
                                .clickable {
                                    expanded = !expanded
                                }
                                .border(
                                    width = screenWidth(x = 1.0),
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(screenWidth(x = 8.0))
                                )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(
                                        screenWidth(x = 16.0)
                                    )
                            ) {
                                Text(
                                    text = selectedStage,
                                    fontSize = screenFontSize(x = 14.0).sp
                                )
                                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                                Icon(
                                    imageVector = if(expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = !expanded
                            },
                        ) {
                            if(expanded) {
                                for (stage in stages) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = stage)
                                        },
                                        onClick = {
                                            selectedStage = stage
                                            onChangeStatus(selectedStage)
                                            expanded = !expanded
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
//                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                if(filtering) {
                    ElevatedCard(
                        shape = RoundedCornerShape(0),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(
                                    horizontal = screenWidth(x = 16.0),
                                    vertical = screenHeight(x = 16.0)
                                )
                                .animateContentSize(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                                .fillMaxWidth()
                        ) {
                            TextField(
                                shape = RoundedCornerShape(screenWidth(x = 10.0)),
                                leadingIcon = {
                                    IconButton(onClick = onFilter) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back"
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = "Search",
                                        fontSize = screenFontSize(x = 14.0).sp
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                                value = searchText,
                                trailingIcon = {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.inverseOnSurface)
                                            .padding(screenWidth(x = 5.0))
                                            .clickable {
                                                onClearSearch()
                                            }

                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear search",
                                            modifier = Modifier
                                                .size(screenWidth(x = 16.0))
                                        )
                                    }

                                },
                                onValueChange = onChangeSearchText,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                            DateRangePickerDialog(
                                startDate = startDate,
                                endDate = endDate,
                                defaultStartDate = null,
                                defaultEndDate = null,
                                onChangeStartDate = onChangeStartDate,
                                onChangeLastDate = onChangeEndDate,
                                onDismiss = { /*TODO*/ },
                                onConfirm = { /*TODO*/ }
                            )
                            TextButton(
                                onClick = onFilter,
                                modifier = Modifier
                                    .align(Alignment.End)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Dismiss filtering"
                                    )
                                    Text(
                                        text = "Dismiss",
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Showing $currentCount of $totalCount users",
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = onFilter) {
                            Icon(
                                painter = painterResource(id = R.drawable.filter),
                                contentDescription = "Filter users"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                if(loadingStatus == LoadingStatus.LOADING) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        PullRefreshIndicator(
                            refreshing = true,
                            state = pullRefreshState!!
                        )
                    }
                } else {
                    if(users.isEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                text = if(selectedStage == "All") "No users found" else "No $selectedStage users",
                                fontSize = screenFontSize(x = 16.0).sp
                            )
                        }
                    } else {
                        LazyColumn {
                            items(users) {user ->
                                UserCell(
                                    user = user,
                                    navigateToUserDetailsScreen = navigateToUserDetailsScreen
                                )
                                Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
                            }
                        }
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                enabled = currentPage > 1,
                onClick = {
                    onChangePage(currentPage - 1)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous page"
                )
            }
            Text(text = "$currentPage / $totalPages")
            IconButton(
                enabled = currentPage < totalPages,
                onClick = {
                    onChangePage(currentPage + 1)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Previous page"
                )
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserCell(
    user: UserVerificationData,
    navigateToUserDetailsScreen: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = screenWidth(x = 1.0),
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(screenWidth(x = 8.0))
            )
    ) {
        Column(
            modifier = Modifier
                .padding(screenWidth(x = 16.0))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = user.username,
                    fontSize = screenFontSize(x = 14.0).sp

                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .border(
                            width = screenWidth(x = 1.0),
                            color = Color.LightGray,
                            shape = RoundedCornerShape(screenWidth(x = 16.0))
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(screenWidth(x = 8.0))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.user_details),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = user.verificationStatus.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() },
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        when(VerificationStatus.valueOf(user.verificationStatus)) {
                            VerificationStatus.PENDING_VERIFICATION -> {
                                Icon(
                                    painter = painterResource(id = R.drawable.pending),
                                    contentDescription = "Pending verification"
                                )
                            }

                            VerificationStatus.UNVERIFIED -> {
                                Icon(
                                    painter = painterResource(id = R.drawable.unverified),
                                    contentDescription = "Unverified",
                                    modifier = Modifier
                                        .size(screenWidth(x = 24.0))
                                )
                            }
                            VerificationStatus.VERIFIED -> {
                                Icon(
                                    painter = painterResource(id = R.drawable.verified),
                                    contentDescription = "Verified"
                                )
                            }
                        }


                    }
                }
            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.phone),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = user.phoneNumber,
                    fontSize = screenFontSize(x = 14.0).sp
                )

            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                Text(
                    text = user.email,
                    fontSize = screenFontSize(x = 14.0).sp
                )

            }
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Text(
                text = "Created on:",
                fontSize = screenFontSize(x = 14.0).sp,
//                fontStyle = FontStyle.Italic,
//                fontWeight = FontWeight.W500
            )
            Text(
                text = formatIsoDateTime2(LocalDateTime.parse(user.createdAt)),
                fontSize = screenFontSize(x = 14.0).sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            OutlinedButton(
                onClick = {
                    navigateToUserDetailsScreen(user.userId.toString())
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "See more",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePickerDialog(
    startDate: LocalDate,
    endDate: LocalDate,
    defaultStartDate: String?,
    defaultEndDate: String?,
    onChangeStartDate: (date: LocalDate) -> Unit,
    onChangeLastDate: (date: LocalDate) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if(defaultStartDate != null) {
            Text(
                text = "Select date range (within $defaultStartDate and $defaultEndDate)",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 18.0).sp,
                modifier = Modifier
                    .padding(
                        start = screenWidth(x = 16.0)
                    )
            )
        } else {
            Text(
                text = "Select date range",
                fontWeight = FontWeight.Bold,
                fontSize = screenFontSize(x = 14.0).sp,
                modifier = Modifier
                    .padding(
                        start = screenWidth(x = 16.0)
                    )
            )
        }

        DateRangePicker(
            startDate = startDate,
            endDate = endDate,
            defaultStartDate = defaultStartDate,
            defaultEndDate = defaultEndDate,
            onChangeStartDate = onChangeStartDate,
            onChangeLastDate = onChangeLastDate,
            modifier = Modifier
                .fillMaxWidth()
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRangePicker(
    startDate: LocalDate,
    endDate: LocalDate,
    defaultStartDate: String?,
    defaultEndDate: String?,
    onChangeStartDate: (date: LocalDate) -> Unit,
    onChangeLastDate: (date: LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
    val context = LocalContext.current

    // Parse the default start and end dates
    val defaultStartLocalDate = defaultStartDate?.let { LocalDate.parse(it) }
    val defaultEndLocalDate = defaultEndDate?.let { LocalDate.parse(it) }

    // Convert LocalDate to milliseconds since epoch
    val defaultStartMillis = defaultStartLocalDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    val defaultEndMillis = defaultEndLocalDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    val oneMonthAgo = LocalDateTime.now().minusMonths(1)

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDatePicker(isStart: Boolean) {
        val initialDate = if (isStart) startDate else endDate
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                if (isStart) {
                    if (selectedDate.isBefore(endDate) || selectedDate.isEqual(endDate)) {
                        onChangeStartDate(selectedDate)
                    } else {
                        // Handle case where start date is after end date
                        Toast.makeText(context, "Start date must be before end date", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (selectedDate.isAfter(startDate) || selectedDate.isEqual(startDate)) {
                        onChangeLastDate(selectedDate)
                    } else {
                        // Handle case where end date is before start date
                        Toast.makeText(context, "End date must be after start date", Toast.LENGTH_LONG).show()
                    }
                }
            },

            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        )

        // Set minimum and maximum dates
        defaultStartMillis?.let { datePicker.datePicker.minDate = it }
        defaultEndMillis?.let { datePicker.datePicker.maxDate = it }

        datePicker.show()
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(onClick = { showDatePicker(true) }) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                modifier = Modifier
                    .size(screenWidth(x = 24.0))
            )
        }
        Text(
            text = dateFormatter.format(startDate),
            fontSize = screenFontSize(x = 14.0).sp
        )
        Text(
            text = "to",
            fontSize = screenFontSize(x = 14.0).sp
        )

        Text(
            text = dateFormatter.format(endDate),
            fontSize = screenFontSize(x = 14.0).sp
        )
        IconButton(onClick = { showDatePicker(false) }) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                modifier = Modifier
                    .size(screenWidth(x = 24.0))
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UsersScreenPreview() {
    WazipayadminTheme {
        UsersScreen(
            adminUsername = "Alex",
            pullRefreshState = null,
            totalPages = 1,
            currentPage = 1,
            currentCount = 8,
            totalCount = 8,
            startDate = LocalDate.now().withDayOfMonth(1),
            endDate = LocalDate.now(),
            onChangeStartDate = {},
            onChangeEndDate = {},
            onFilter = {},
            filtering = false,
            onClearSearch = {},
            onChangeSearchText = {},
            searchText = "",
            onChangePage = {},
            onChangeStatus = {},
            loadingStatus = LoadingStatus.INITIAL,
            navigateToUserDetailsScreen = {},
            users = userVerifications
        )
    }
}