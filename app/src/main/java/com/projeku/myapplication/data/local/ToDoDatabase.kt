package com.projeku.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.projeku.myapplication.domain.model.ToDo

@Database(entities = [ToDo::class], version = 1, exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}