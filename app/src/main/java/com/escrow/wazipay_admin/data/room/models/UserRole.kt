package com.escrow.wazipay_admin.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserRole(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val role: Role
)
