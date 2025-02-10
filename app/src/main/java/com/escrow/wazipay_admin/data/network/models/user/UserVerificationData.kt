package com.escrow.wazipay_admin.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserVerificationData(
    val id: Int,
    val userId: Int,
    val username: String,
    val phoneNumber: String,
    val email: String,
    val verified: Boolean?,
    val verificationStatus: String,
    val createdAt: String,
    val verifiedAt: String?,
    val roles: List<String>,
    val idFront: String?,
    val idBack: String?,
    val verifiedBy: UserDetailsData,
)
