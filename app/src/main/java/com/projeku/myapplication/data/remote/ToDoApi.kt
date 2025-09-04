package com.projeku.myapplication.data.remote

import retrofit2.http.GET

data class ToDoDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)

interface ToDoApi {
    @GET("todos")
    suspend fun getToDos(): List<ToDoDto>
}