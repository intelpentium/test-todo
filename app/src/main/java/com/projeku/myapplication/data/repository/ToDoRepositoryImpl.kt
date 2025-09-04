package com.projeku.myapplication.data.repository

import com.projeku.myapplication.data.local.ToDoDao
import com.projeku.myapplication.data.local.ToDoEntity
import com.projeku.myapplication.data.remote.ToDoApi
import com.projeku.myapplication.data.remote.ToDoDto
import com.projeku.myapplication.domain.model.ToDo
import com.projeku.myapplication.domain.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ToDoRepositoryImpl(
    private val api: ToDoApi,
    private val dao: ToDoDao,
    override val simulateOffline: Boolean = false
) : ToDoRepository {

    override fun getToDos(): Flow<List<ToDo>> {
        return dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun fetchToDosFromApiAndCache(): Result<Unit> {
        return try {
            if (simulateOffline) {
                Result.failure(Exception("Offline mode"))
            } else {
                val remote = api.getToDos()
                val entities = remote.map { it.toEntity() }
                dao.insertAll(entities)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateToDoLocal(todo: ToDo) {
        dao.update(todo.toEntity())
    }

    private fun ToDoEntity.toDomain() =
        ToDo(id = id, userId = userId, title = title, completed = completed)

    private fun ToDoDto.toEntity() =
        ToDoEntity(id = id, userId = userId, title = title, completed = completed)

    private fun ToDo.toEntity() =
        ToDoEntity(id = id, userId = userId, title = title, completed = completed)
}