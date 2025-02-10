package com.escrow.wazipay_admin.ui.screen.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.escrow.wazipay.utils.formatIsoDateTime2
import com.escrow.wazipay.utils.screenFontSize
import com.escrow.wazipay.utils.screenHeight
import com.escrow.wazipay.utils.screenWidth
import com.escrow.wazipay_admin.AppViewModelFactory
import com.escrow.wazipay_admin.R
import com.escrow.wazipay_admin.data.network.models.user.UserVerificationData
import com.escrow.wazipay_admin.data.network.models.user.VerificationStatus
import com.escrow.wazipay_admin.data.network.models.user.userVerificationData
import com.escrow.wazipay_admin.ui.nav.AppNavigation
import com.escrow.wazipay_admin.ui.theme.WazipayadminTheme
import java.time.LocalDateTime

object UserDetailsScreenDestination: AppNavigation {
    override val title: String = "User Details"
    override val route: String = "user-details"
    val userId: String = "userId"
    val routeWithArgs: String = "$route/{$userId}"
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserDetailsScreenComposable(
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val viewModel: UserDetailsViewModel = viewModel(factory = AppViewModelFactory.Factory)
    val uiState by viewModel.uiState.collectAsState()

    var showVerifyUserDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.loadingStatus == LoadingStatus.LOADING,
        onRefresh = {
            viewModel.initializeData()
        }
    )

    if(uiState.loadingVerificationStatus == LoadingVerificationStatus.SUCCESS) {
        showVerifyUserDialog = false
        viewModel.initializeData()
        Toast.makeText(context, "User verified successfully", Toast.LENGTH_SHORT).show()
        viewModel.resetStatus()
    }

    if(showVerifyUserDialog) {
        VerifyUserDialog(
            onConfirm = {
                showVerifyUserDialog = !showVerifyUserDialog
                viewModel.verifyUser()
            },
            onDismiss = {
                showVerifyUserDialog = !showVerifyUserDialog
            }
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
    ) {
        UserDetailsScreen(
            pullRefreshState = pullRefreshState,
            user = uiState.user,
            loadingStatus = uiState.loadingStatus,
            loadingVerificationStatus = uiState.loadingVerificationStatus,
            onVerify = {
                showVerifyUserDialog = !showVerifyUserDialog
            },
            navigateToPreviousScreen = navigateToPreviousScreen,

        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserDetailsScreen(
    pullRefreshState: PullRefreshState?,
    user: UserVerificationData,
    loadingStatus: LoadingStatus,
    loadingVerificationStatus: LoadingVerificationStatus,
    onVerify: () -> Unit,
    navigateToPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = screenHeight(x = 16.0),
                horizontal = screenWidth(x = 16.0)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = screenWidth(x = 16.0))
        ) {
            IconButton(onClick = navigateToPreviousScreen) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous screen"
                )
            }
            Text(
                text = "User details",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = screenFontSize(x = 16.0).sp
            )
        }
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
            Row {
                Column {
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
                    Text(
                        text = "Requested verification on:",
                        fontSize = screenFontSize(x = 14.0).sp,
                    )
                    Text(
                        text = if(user.verificationRequestedOn == null) "N/A" else formatIsoDateTime2(LocalDateTime.parse(user.verificationRequestedOn)),
                        fontSize = screenFontSize(x = 14.0).sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Verification status:",
                            fontSize = screenFontSize(x = 14.0).sp
                        )
                        Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                        Text(
                            text = user.verificationStatus.lowercase()
                                .replace("_", " ")
                                .replaceFirstChar { it.uppercase() },
                            fontSize = screenFontSize(x = 14.0).sp,
                            color = when (VerificationStatus.valueOf(user.verificationStatus)) {
                                VerificationStatus.UNVERIFIED -> Color(0xFFD32F2F) // Red
                                VerificationStatus.PENDING_VERIFICATION -> Color(0xFFFFA000) // Orange
                                VerificationStatus.VERIFIED -> Color(0xFF388E3C) // Green
                            }
                        )
                    }

                    if(user.verificationStatus == "VERIFIED") {
                        Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                        Text(
                            text = "Verified on:",
                            fontSize = screenFontSize(x = 14.0).sp,
                        )
                        Text(
                            text = if(user.verifiedAt == null) "N/A" else formatIsoDateTime2(LocalDateTime.parse(user.verifiedAt)),
                            fontSize = screenFontSize(x = 14.0).sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        text = "Roles:",
                        fontWeight = FontWeight.Bold,
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                    Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
                    user.roles.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.circle),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(screenWidth(x = 8.0))
                            )
                            Spacer(modifier = Modifier.width(screenWidth(x = 4.0)))
                            Text(
                                text = it.lowercase().replaceFirstChar { it.uppercase() },
                                fontSize = screenFontSize(x = 14.0).sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(screenHeight(x = 8.0)))
            Text(
                text = "Verification documents",
                fontSize = screenFontSize(x = 14.0).sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(screenHeight(x = 16.0)))
            if(user.idFront != null) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(user.idFront)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.ic_broken_image),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Front ID",
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth(0.7f)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.width(screenWidth(x = 16.0)))
                    Row {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(user.idBack)
                                .crossfade(true)
                                .build(),
                            placeholder = painterResource(id = R.drawable.loading_img),
                            error = painterResource(id = R.drawable.ic_broken_image),
                            contentScale = ContentScale.Crop,
                            contentDescription = "Front ID",
                            modifier = Modifier
                                .height(250.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = "No verification documents",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                enabled = user.verificationStatus == "PENDING_VERIFICATION" && loadingVerificationStatus != LoadingVerificationStatus.LOADING,
                onClick = onVerify,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if(loadingVerificationStatus == LoadingVerificationStatus.LOADING) {
                    Text(
                        text = "Verifying...",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                } else {
                    Text(
                        text = "Verify user",
                        fontSize = screenFontSize(x = 14.0).sp
                    )
                }

            }
        }

    }
}

@Composable
fun VerifyUserDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        title = {
            Text(
                text = "Verify user",
                fontSize = screenFontSize(x = 16.0).sp,
            )
        },
        text = {
            Text(
                text = "Are you sure you want to verify this user?",
                fontSize = screenFontSize(x = 14.0).sp
            )
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    text = "Confirm verification",
                    fontSize = screenFontSize(x = 14.0).sp
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserDetailsScreenPreview() {
    WazipayadminTheme {
        UserDetailsScreen(
            pullRefreshState = null,
            user = userVerificationData,
            loadingStatus = LoadingStatus.INITIAL,
            loadingVerificationStatus = LoadingVerificationStatus.INITIAL,
            onVerify = {},
            navigateToPreviousScreen = {}
        )
    }
}