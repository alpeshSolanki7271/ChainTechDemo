package com.example.chaintechdemo.repository

import com.example.chaintechdemo.data.model.PasswordData
import com.example.chaintechdemo.database.PasswordDao
import javax.inject.Inject


class AppRepository @Inject constructor(private val passwordDao: PasswordDao) {

    suspend fun addAccount(passwordData: PasswordData) {
        passwordDao.addAccount(passwordData = passwordData)
    }

    suspend fun getPasswordData(): List<PasswordData> {
        return passwordDao.getAllPasswordData()
    }

    suspend fun updatePasswordData(passwordData: PasswordData) {
        return passwordDao.updatePasswordDataFields(
            id = passwordData.id,
            accountType = passwordData.accountType,
            username = passwordData.username,
            password = passwordData.password,
        )
    }

    suspend fun deletePasswordData(id: Int) {
        return passwordDao.deletePasswordDataById(id = id)
    }


}