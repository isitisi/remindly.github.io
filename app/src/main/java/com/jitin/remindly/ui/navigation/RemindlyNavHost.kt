package com.jitin.remindly.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jitin.remindly.ui.event.EventEditScreen
import com.jitin.remindly.ui.home.HomeScreen
import com.jitin.remindly.ui.task.TaskEditScreen

private object Routes {
    const val HOME = "home"
    const val TASK_EDIT = "task_edit/{taskId}"
    const val EVENT_EDIT = "event_edit/{eventId}"
    fun taskEdit(id: Long) = "task_edit/$id"
    fun eventEdit(id: Long) = "event_edit/$id"
}

@Composable
fun RemindlyNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onAddTask = { navController.navigate(Routes.taskEdit(0)) },
                onEditTask = { id -> navController.navigate(Routes.taskEdit(id)) },
                onAddEvent = { navController.navigate(Routes.eventEdit(0)) },
                onEditEvent = { id -> navController.navigate(Routes.eventEdit(id)) }
            )
        }
        composable(
            Routes.TASK_EDIT,
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: 0L
            TaskEditScreen(taskId = taskId, onDone = { navController.popBackStack() })
        }
        composable(
            Routes.EVENT_EDIT,
            arguments = listOf(navArgument("eventId") { type = NavType.LongType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: 0L
            EventEditScreen(eventId = eventId, onDone = { navController.popBackStack() })
        }
    }
}
