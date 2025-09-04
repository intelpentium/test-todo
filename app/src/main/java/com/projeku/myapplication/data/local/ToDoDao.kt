package com.projeku.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.projeku.myapplication.domain.model.ToDo
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<ToDo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodos(todos: List<ToDo>)

    @Update
    suspend fun updateTodo(todo: ToDo)

    @Query("DELETE FROM todos")
    suspend fun clear()
}