package com.escrow.wazipay_admin.data.network.models.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseBody(
    val statusCode: Int,
    val message: String,
    val data: LoginResponseBodyData
)

@Serializable
data class LoginResponseBodyData(
    val user: LoginResponseBodyDataUser,
    val token: String
)

@Serializable
data class LoginResponseBodyDataUser(
    val userId: Int,
    val username: String,
    val phoneNumber: String,
    val email: String,
    val roles: List<String>,
    val createdAt: String,
    val verified: Boolean,
    val verifiedAt: String?,
    val verificationStatus: String?,
    val archived: Boolean,
    val archivedAt: String?
)


