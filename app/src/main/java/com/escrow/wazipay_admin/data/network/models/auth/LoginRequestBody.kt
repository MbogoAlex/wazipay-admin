package com.escrow.wazipay_admin.data.network.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestBody(
    val phoneNumber: String,
    val pin: String,
)
