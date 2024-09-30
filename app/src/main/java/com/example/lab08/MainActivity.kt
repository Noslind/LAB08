package com.example.lab08
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.room.Room
import kotlinx.coroutines.launch

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
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
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
                Button(onClick = { viewModel.toggleTaskCompletion(task) }) {
                    Text(if (task.isCompleted) "Completada" else "Pendiente")
                }
            }
        }
        Button(onClick = { viewModel.deleteAllTasks() }) {
            Text("Eliminar todas las tareas")
        }
    }
}
