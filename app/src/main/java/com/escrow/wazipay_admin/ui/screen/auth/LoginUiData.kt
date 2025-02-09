package com.escrow.wazipay_admin.ui.screen.auth

data class LoginUiData(
    val phoneNumber: String = "",
    val pin: String = "",
    val loginStatus: LoginStatus = LoginStatus.INITIAL,
    val loginMessage: String = "",
    val buttonEnabled: Boolean = false
)
