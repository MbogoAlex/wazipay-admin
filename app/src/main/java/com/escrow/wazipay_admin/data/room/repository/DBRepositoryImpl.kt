package com.escrow.wazipay_admin.data.room.repository

import com.escrow.wazipay_admin.data.room.AppDao
import com.escrow.wazipay_admin.data.room.models.DarkMode
import com.escrow.wazipay_admin.data.room.models.UserDetails
import com.escrow.wazipay_admin.data.room.models.UserRole
import kotlinx.coroutines.flow.Flow

class DBRepositoryImpl(private val appDao: AppDao): DBRepository {
    override fun insertUser(userDetails: UserDetails) =
        appDao.insertUser(userDetails)

    override suspend fun updateUser(userDetails: UserDetails) =
        appDao.updateUser(userDetails)

    override fun getUsers(): Flow<List<UserDetails>> =
        appDao.getUsers()

    override fun getUser(userId: Int): Flow<UserDetails> =
        appDao.getUser(userId)

    override suspend fun deleteUsers() =
        appDao.deleteUsers()

    override suspend fun createTheme(darkMode: DarkMode) =
        appDao.createTheme(darkMode)

    override suspend fun changeTheme(darkMode: DarkMode) =
        appDao.changeTheme(darkMode)

    override fun getTheme(): Flow<DarkMode?> =
        appDao.getTheme()

    override suspend fun insertUserRole(userRole: UserRole) =
        appDao.insertUserRole(userRole)

    override suspend fun updateUserRole(userRole: UserRole) =
        appDao.updateUserRole(userRole)

    override fun getUserRole(): Flow<UserRole?> =
        appDao.getUserRole()
}