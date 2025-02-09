package com.escrow.wazipay_admin.ui.screen.start

import com.escrow.wazipay_admin.data.room.models.UserDetails

data class SplashUiData(
    val userDetails: UserDetails = UserDetails(),
    val isLoading: Boolean = true,
    val isNavigating: Boolean = true,
)
