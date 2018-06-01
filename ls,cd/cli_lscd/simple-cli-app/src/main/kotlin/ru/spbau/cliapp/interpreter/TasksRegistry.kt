package ru.spbau.cliapp.interpreter

import ru.spbau.cliapp.task.Task

/**
 * Class for keeping [Task] instances by names along with some default fallback task.
 *
 * [defaultTaskCreator] is a function that creates tasks by name.
 */
class TasksRegistry(private val defaultTaskCreator: (String) -> Task, tasks: Map<String, Task> = emptyMap()) {
    private val tasks: MutableMap<String, Task> = tasks.toMutableMap()

    /**
     * Adds task by this name into the registry, replaces existing.
     */
    fun addTask(name: String, task: Task) {
        tasks[name] = task
    }

    /**
     * Returns task found by this name or created by [defaultTaskCreator].
     */
    fun getTaskByName(name: String): Task = tasks[name] ?: defaultTaskCreator(name)

}
