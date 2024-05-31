package com.example.chaintechdemo.screens.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chaintechdemo.data.model.PasswordData
import com.example.chaintechdemo.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {

    private val _passwordDataList = MutableLiveData<List<PasswordData>>()
    val passwordDataList: LiveData<List<PasswordData>> = _passwordDataList

    fun fetchUserList() {
        viewModelScope.launch {
            val userList = getUserList()
            _passwordDataList.postValue(userList)
        }
    }

    private suspend fun getUserList(): List<PasswordData> {
        return try {
            withContext(Dispatchers.IO) {
                appRepository.getPasswordData()
            }
        } catch (e: Exception) {
            Log.e("TAG", "getUserList: $e")
            emptyList()
        }
    }

    suspend fun addAccount(passwordData: PasswordData) {
        try {
            appRepository.addAccount(passwordData = passwordData)
        } catch (e: Exception) {
            Log.e("TAG", "addAccount: $e")
        }
    }

    suspend fun updateData(passwordData: PasswordData) {
        try {
            appRepository.updatePasswordData(passwordData = passwordData)
        } catch (e: Exception) {
            Log.e("TAG", "addAccount: $e")
        }
    }

    suspend fun deleteData(id: Int) {
        try {
            appRepository.deletePasswordData(id = id)
        } catch (e: Exception) {
            Log.e("TAG", "addAccount: $e")
        }
    }

}