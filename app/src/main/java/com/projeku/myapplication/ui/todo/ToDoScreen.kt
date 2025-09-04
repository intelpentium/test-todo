package com.projeku.myapplication.ui.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.projeku.myapplication.domain.model.ToDo
import com.projeku.myapplication.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(
    viewModel: ToDoViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("To-Do List") },
                actions = {
                    IconButton(onClick = { viewModel.refreshFromApi() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            when (uiState) {
                is UiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text("Loading...")
                    }
                }
                is UiState.Success -> {
                    ToDoList(items = (uiState as UiState.Success).data, onToggle = { viewModel.toggleCompleted(it) })
                }
                is UiState.Offline -> {
                    val cached = (uiState as UiState.Offline).cached
                    Column {
                        Text(
                            text = "Offline (simulated). Showing cached data",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        ToDoList(items = cached, onToggle = { viewModel.toggleCompleted(it) })
                    }
                }
                is UiState.Error -> {
                    val msg = (uiState as UiState.Error).message
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: $msg", modifier = Modifier.padding(8.dp))
                        Button(onClick = { viewModel.refreshFromApi() }) {
                            Text("Retry / Refresh")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ToDoList(items: List<ToDo>, onToggle: (ToDo) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items) { item ->
            ToDoItem(item = item, onToggle = onToggle)
        }
    }
}

@Composable
fun ToDoItem(item: ToDo, onToggle: (ToDo) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
    ) {
        ListItem(
            headlineContent = { Text(item.title) },
            supportingContent = { Text("ID: ${item.id} - user: ${item.userId}") },
            trailingContent = {
                Checkbox(
                    checked = item.completed,
                    onCheckedChange = { onToggle(item) }
                )
            }
        )
    }
}