package com.projeku.myapplication.domain.repository

import com.projeku.myapplication.domain.model.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    val simulateOffline: Boolean

    fun getToDos(): Flow<List<ToDo>>
    suspend fun fetchToDosFromApiAndCache(): Result<Unit>
    suspend fun updateToDoLocal(todo: ToDo)
}