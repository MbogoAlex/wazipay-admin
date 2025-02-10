package com.escrow.wazipay_admin.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserVerificationResponseBody(
    val statusCode: Int,
    val message: String,
    val data: UserVerificationData
)
