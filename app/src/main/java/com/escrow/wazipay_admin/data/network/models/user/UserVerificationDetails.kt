package com.escrow.wazipay_admin.data.network.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserVerificationDetails(
    val verificationId: Int?,
    val userId: Int?,
    val username: String?,
    val phoneNumber: String?,
    val email: String?,
    val idFront: String?,
    val idBack: String?,
    val verificationStatus: String?,
    val verifiedAt: String?,
)
