package com.projeku.myapplication.utils

import com.projeku.myapplication.domain.model.ToDo

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<ToDo>) : UiState()
    data class Error(val message: String) : UiState()
    data class Offline(val cached: List<ToDo>) : UiState()
}