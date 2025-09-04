package com.projeku.myapplication.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projeku.myapplication.domain.model.ToDo
import com.projeku.myapplication.domain.repository.ToDoRepository
import com.projeku.myapplication.utils.UiState
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ToDoViewModel @Inject constructor(
    private val repo: ToDoRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<ToDo>>>(UiState.Loading)
    val state: StateFlow<UiState<List<ToDo>>> = _state

    init {
        observe()
        refresh()
    }

    private fun observe() {
        viewModelScope.launch {
            repo.observeTodos()
                .onStart {
                    if (repo.simulateOffline) _state.value = UiState.Offline
                }
                .catch { e ->
                    _state.value = UiState.Error(e.message ?: "Error")

                }
                .collect { list ->
                    _state.value = UiState.Success(list)
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                if (repo.simulateOffline) {
                    _state.value = UiState.Offline
                } else {
                    _state.value = UiState.Loading
                    repo.refreshFromRemote()
                }
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Refresh Failed")
            }
        }
    }

    fun toggle(todo: ToDo, checked: Boolean){
        viewModelScope.launch {
            repo.updateLocal(todo.copy(completed = checked))
        }
    }
}