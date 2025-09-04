package com.projeku.myapplication.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class ToDo (
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean,
)