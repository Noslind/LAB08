package com.example.lab08.base_de_datos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab08.dao.TaskDao
import com.example.lab08.modelo.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
