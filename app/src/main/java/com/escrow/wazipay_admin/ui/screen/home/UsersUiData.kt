package com.escrow.wazipay_admin.ui.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import com.escrow.wazipay_admin.data.network.models.user.UserVerificationData
import com.escrow.wazipay_admin.data.network.models.user.VerificationStatus
import com.escrow.wazipay_admin.data.room.models.UserDetails
import java.time.LocalDate

data class UsersUiData(
    val userDetails: UserDetails = UserDetails(),
    val totalPages: Int = 0,
    val currentPage: Int = 1,
    val currentCount: Int = 0,
    val totalCount: Int = 0,
    val searchText: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val status: String = "All",
    val verificationStatus: VerificationStatus? = null,
    val unauthorized: Boolean = false,
    val users: List<UserVerificationData> = emptyList(),
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING
)
