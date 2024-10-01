package com.example.lab08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room.databaseBuilder
import com.example.lab08.base_de_datos.TaskDatabase
import com.example.lab08.viewmodel.TaskViewModel


import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db = Room.databaseBuilder(
                applicationContext,
                TaskDatabase::class.java, "task_db"
            ).build()
            val taskDao = db.taskDao()
            val viewModel = TaskViewModel(taskDao)

            TaskScreen(viewModel)
        }
    }
}

@Composable
fun TaskScreen(viewModel: TaskViewModel = viewModel()) {
    val tasks by viewModel.tasks.collectAsState(emptyList())
    var newTaskDescription by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = newTaskDescription,
            onValueChange = { newTaskDescription = it },
            label = { Text("Nueva tarea") }
        )
        Button(onClick = {
            if (newTaskDescription.isNotEmpty()) {
                viewModel.addTask(newTaskDescription)
                newTaskDescription = ""
            }
        }) {
            Text("Agregar tarea")
        }
        tasks.forEach { task ->
            Row {
                Text(text = task.description)
                Button(onClick = { viewModel.updateTask(task.copy(isCompleted = !task.isCompleted)) }) {
                    Text(if (task.isCompleted) "Completada" else "Pendiente")
                }
                Button(onClick = { viewModel.deleteTask(task) }) {
                    Text("Eliminar")
                }
            }
        }
        Button(onClick = { viewModel.deleteAllTasks() }) {
            Text("Eliminar todas las tareas")
        }
    }
}
