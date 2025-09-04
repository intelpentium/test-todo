package com.projeku.myapplication.domain.usecase

import com.projeku.myapplication.domain.model.ToDo
import com.projeku.myapplication.domain.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow


class GetToDoListUseCase(private val repository: ToDoRepository) {
    operator fun invoke(): Flow<List<ToDo>> = repository.getToDos()
}