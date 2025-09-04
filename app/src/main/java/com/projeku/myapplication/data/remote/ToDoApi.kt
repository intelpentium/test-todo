package com.projeku.myapplication.data.remote

import com.projeku.myapplication.domain.model.ToDo
import retrofit2.http.GET

interface ToDoApi {
    @GET("todos")
    suspend fun getTodos(): List<ToDo>
}