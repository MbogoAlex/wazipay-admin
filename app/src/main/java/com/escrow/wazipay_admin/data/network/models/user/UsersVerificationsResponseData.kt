package com.escrow.wazipay_admin.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UsersVerificationsResponseData(
    val data: List<UserVerificationData>,
    val size: Int,
    val currentCount: Int,
    val totalPages: Int,
    val page: Int,
    val totalCount: Int,
)
