package com.smartai.kmp.presentation.screen.main_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontVariation.Setting
import androidx.lifecycle.ViewModel
import com.smartai.kmp.domain.usecase.GetAllGroupUseCase
import com.smartai.kmp.domain.usecase.InsertGroupUseCase
import com.smartai.kmp.domain.usecase.InsertMessageUseCase
import com.smartai.kmp.utils.AppCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val appCoroutineDispatcher: AppCoroutineDispatcher,
    private val getAllGroupUseCase: GetAllGroupUseCase,
    private val insertGroupUseCase: InsertGroupUseCase
) : ViewModel() {

    private val settings = FontVariation.Settings()

    val screens by mutableStateOf("")

    var isApiShowingDialog by mutableStateOf(false)
        private set

    var apiKeyText by mutableStateOf("AIzaSyAz3vTNj3plb6_ZH2zCtAg2O22dhY2aeMQ")
        private set

    var isAlertDialogShow by mutableStateOf(false)
        private set

    var alertTitleText by mutableStateOf("")
        private set

    var alertDescText by mutableStateOf("")
        private set
    var currentPos by mutableStateOf(-1)
        private set

    private val _uiState = MutableStateFlow(GroupUiState())
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()

    init {
        getGroupList()
    }

    fun getGroupList() {
        viewModelScope.launch(appCoroutineDispatcher.io) {
            _uiState.update { GroupUiState(getAllGroupUseCase()) }
        }
    }

}