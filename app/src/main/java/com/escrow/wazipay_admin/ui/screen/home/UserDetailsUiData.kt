package com.escrow.wazipay_admin.ui.screen.home

import com.escrow.wazipay_admin.data.network.models.user.UserVerificationData
import com.escrow.wazipay_admin.data.network.models.user.userVerifications
import com.escrow.wazipay_admin.data.room.models.UserDetails

data class UserDetailsUiData(
    val userDetails: UserDetails = UserDetails(),
    val user: UserVerificationData = userVerifications[0],
    val loadingStatus: LoadingStatus = LoadingStatus.LOADING,
    val loadingVerificationStatus: LoadingVerificationStatus = LoadingVerificationStatus.INITIAL
)
