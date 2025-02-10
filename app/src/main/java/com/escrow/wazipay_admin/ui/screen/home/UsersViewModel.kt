package com.escrow.wazipay_admin.ui.screen.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escrow.wazipay_admin.data.network.models.user.VerificationStatus
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
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class UsersViewModel(
    private val apiRepository: ApiRepository,
    private val dbRepository: DBRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiData())
    val uiState: StateFlow<UsersUiData> = _uiState.asStateFlow()

    fun changeSearchText(text: String) {
        _uiState.update {
            it.copy(
                searchText = text
            )
        }

        getUsers()
    }

    fun changeStartDate(startDate: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = startDate.toString()
            )
        }

        getUsers()
    }

    fun changeEndDate(endDate: LocalDate) {
        _uiState.update {
            it.copy(
                endDate = endDate.toString()
            )
        }

        getUsers()
    }

    fun changeStatus(status: String) {
        _uiState.update {
            it.copy(
                status = status
            )
        }

        when(status) {
            "All" -> {
                _uiState.update {
                    it.copy(
                        verificationStatus = null
                    )
                }
            }
            "Unverified" -> {
                _uiState.update {
                    it.copy(
                        verificationStatus = VerificationStatus.UNVERIFIED
                    )
                }
            }
            "Pending verification" -> {
                _uiState.update {
                    it.copy(
                        verificationStatus = VerificationStatus.PENDING_VERIFICATION
                    )
                }
            }
            "Verified" -> {
                _uiState.update {
                    it.copy(
                        verificationStatus = VerificationStatus.VERIFIED
                    )
                }
            }
        }
        getUsers()
    }

    fun changePage(page: Int) {
        _uiState.update {
            it.copy(
                currentPage = page
            )
        }
        getUsers()
    }

    private fun getUsers() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.LOADING
            )
        }
        viewModelScope.launch {
            try {
               val response = apiRepository.getUsersVerifications(
                   token = uiState.value.userDetails.token!!,
                   userId = null,
                   status = uiState.value.verificationStatus?.name,
                   startDate = LocalDate.parse(uiState.value.startDate),
                   endDate = LocalDate.parse(uiState.value.endDate),
                   page = uiState.value.currentPage - 1,
                   size = 10
               )

                if(response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.SUCCESS,
                            users = response.body()!!.data.data,
                            totalCount = response.body()!!.data.totalCount,
                            totalPages = response.body()!!.data.totalPages,
                            currentPage = response.body()!!.data.page + 1,
                            currentCount = response.body()!!.data.currentCount
                        )
                    }
                } else {
                    if(response.code() == 401) {
                        _uiState.update {
                            it.copy(
                                unauthorized = true
                            )
                        }
                    }

                    _uiState.update {
                        it.copy(
                            loadingStatus = LoadingStatus.FAIL
                        )
                    }

                    Log.e("getUsers", "response_err: $response")
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingStatus = LoadingStatus.FAIL
                    )
                }

                Log.e("getUsers", "exception_err: $e")

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeDates() {
        _uiState.update {
            it.copy(
                startDate = LocalDate.now().withDayOfMonth(1).toString(),
                endDate = LocalDate.now().toString()
            )
        }
    }

    fun initializeData() {
        viewModelScope.launch {
            while (uiState.value.userDetails.userId == 0) {
                delay(2000)
            }
            getUsers()
        }
    }

    fun resetStatus() {
        _uiState.update {
            it.copy(
                loadingStatus = LoadingStatus.INITIAL
            )
        }
    }

    init {
        initializeDates()
        getUser()
        initializeData()
    }
}