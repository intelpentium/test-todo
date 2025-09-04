package com.projeku.myapplication.ui.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.projeku.myapplication.domain.model.ToDo
import com.projeku.myapplication.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(vm: ToDoViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do (Compose)") },
                actions = {
                    TextButton(onClick = vm::refresh) { Text("Refresh") }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (state) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Error -> Text(
                    text = (state as UiState.Error).message,
                    modifier = Modifier.align(Alignment.Center)
                )
                is UiState.Offline -> Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Offline mode (simulated)")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = vm::refresh) { Text("Try Refresh") }
                }
                is UiState.Success -> TodoList(
                    items = (state as UiState.Success<List<ToDo>>).data,
                    onCheck = { t, c -> vm.toggle(t, c) }
                )
            }
        }
    }
}

@Composable
private fun TodoList(items: List<ToDo>, onCheck: (ToDo, Boolean) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        items(items, key = { it.id }) { todo ->
            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                Row(
                    Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = todo.completed,
                        onCheckedChange = { onCheck(todo, it) }
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(todo.title, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}