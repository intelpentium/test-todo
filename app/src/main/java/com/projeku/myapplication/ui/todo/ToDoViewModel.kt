package com.projeku.myapplication.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projeku.myapplication.domain.model.ToDo
import com.projeku.myapplication.domain.repository.ToDoRepository
import com.projeku.myapplication.domain.usecase.GetToDoListUseCase
import com.projeku.myapplication.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val getToDoListUseCase: GetToDoListUseCase,
    private val repository: ToDoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var observeJob: Job? = null

    init {
        observeToDos()
        refreshFromApiIfNeeded()
    }

    private fun observeToDos() {
        observeJob?.cancel()
        observeJob = getToDoListUseCase()
            .onStart { _uiState.value = UiState.Loading }
            .catch { e ->
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
            .onEach { list ->
                if (list.isEmpty()) {
                    if (repository.simulateOffline) {
                        _uiState.value = UiState.Offline(list)
                    } else {
                        _uiState.value = UiState.Success(list)
                    }
                } else {
                    if (repository.simulateOffline) {
                        _uiState.value = UiState.Offline(list)
                    } else {
                        _uiState.value = UiState.Success(list)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun refreshFromApi() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val res = repository.fetchToDosFromApiAndCache()
            if (res.isSuccess) {
            } else {
                val message = res.exceptionOrNull()?.message ?: "Failed to fetch"
                getToDoListUseCase().firstOrNull()?.let { cached ->
                    if (cached.isNotEmpty()) {
                        _uiState.value = UiState.Error("$message — showing cached data")
                    } else {
                        _uiState.value = UiState.Error(message)
                    }
                } ?: run {
                    _uiState.value = UiState.Error(message)
                }
            }
        }
    }

    private fun refreshFromApiIfNeeded() {
        if (!repository.simulateOffline) {
            viewModelScope.launch {
                val res = repository.fetchToDosFromApiAndCache()
                if (res.isFailure) {
                }
            }
        }
    }

    fun toggleCompleted(todo: ToDo) {
        viewModelScope.launch {
            val updated = todo.copy(completed = !todo.completed)
            repository.updateToDoLocal(updated)
        }
    }
}