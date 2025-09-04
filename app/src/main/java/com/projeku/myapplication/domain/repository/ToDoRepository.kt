package com.projeku.myapplication.domain.repository

import com.projeku.myapplication.domain.model.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun observeTodos(): Flow<List<ToDo>>
    suspend fun refreshFromRemote()
    suspend fun updateLocal(todo: ToDo)
    val simulateOffline: Boolean
}