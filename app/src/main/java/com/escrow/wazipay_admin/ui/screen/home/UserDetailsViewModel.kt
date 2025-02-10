package com.escrow.wazipay_admin.ui.screen.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay_admin.data.network.repository.ApiRepository
import com.escrow.wazipay_admin.data.room.models.UserDetails
import com.escrow.wazipay_admin.data.room.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDetailsViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(UserDetailsUiData())
    val uiState: StateFlow<UserDetailsUiData> = _uiState.asStateFlow()

    private val userId: String? = savedStateHandle[UserDetailsScreenDestination.userId]

    private fun getUserVerificationDetails() {
        Log.d("getUserVerificationDt", "userId: $userId")
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }

        viewModelScope.launch {
            try {
               val response = apiRepository.getUserVerification(
                   token = uiState.value.userDetails.token!!,
                   userId = userId!!.toInt()
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            user = response.body()?.data!!,
                            loadingStatus = LoadingStatus.SUCCESS
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }

                    Log.e("getUserVerificationDt", "response: $response")
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("getUserVerificationDt", "e: $e")
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbRepository.getUsers().collect {users ->
                    _uiState.update {
                        it.copy(
                            userDetails = if(users.isEmpty()) UserDetails() else users[0]
                        )
                    }
                }
            }
        }
    }

    fun initializeData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(2000)
            }
            getUserVerificationDetails()
        }
    }

    init {
        getUser()
        initializeData()
    }
}