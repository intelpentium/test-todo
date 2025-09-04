package com.projeku.myapplication.data.repository

import com.projeku.myapplication.data.local.ToDoDao
import com.projeku.myapplication.data.remote.ToDoApi
import com.projeku.myapplication.domain.model.ToDo
import com.projeku.myapplication.domain.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow

class ToDoRepositoryImpl(
    private val api: ToDoApi,
    private val dao: ToDoDao,
    override val simulateOffline: Boolean,
) : ToDoRepository {

    override fun observeTodos(): Flow<List<ToDo>> = dao.getAllTodos()

    override suspend fun refreshFromRemote() {
        if (simulateOffline) return
        val todos = api.getTodos()
        dao.clear()
        dao.insertTodos(todos)

    }

    override suspend fun updateLocal(todo: ToDo) {
        dao.updateTodo(todo)
    }
}