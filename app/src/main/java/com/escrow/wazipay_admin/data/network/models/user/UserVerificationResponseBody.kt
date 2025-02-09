package com.escrow.wazipay_admin.data.network.models.user

import com.escrow.wazipay_admin.data.network.models.user.UserVerificationData
import kotlinx.serialization.Serializable

@Serializable
data class UserVerificationResponseBody(
    val statusCode: Int,
    val message: String,
    val data: UserVerificationData
)
