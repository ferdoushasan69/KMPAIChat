package com.smartai.kmp.presentation.screen.main_screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun MainScreen(
    viewModel: MainViewModel
){
    val groupUiSate by viewModel.uiState.collectAsState()

    LazyColumn {
        items(groupUiSate.data, key = {it.groupId}){
            Text(it.groupName)
        }
    }
}