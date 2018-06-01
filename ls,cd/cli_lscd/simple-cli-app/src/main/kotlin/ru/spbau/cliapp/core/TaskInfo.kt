package ru.spbau.cliapp.core

/**
 * This class represents tasks for interpreter.
 *
 * Currently there's only two types of tasks:
 *
 * - [VarAssignmentInfo] represents assignment (like `x=10`)
 * - [TasksPipeline] represents pipeline of tasks described by [TaskInfo]s
 */
sealed class InterpreterTask

data class VarAssignmentInfo(val varName: String, val value: String) : InterpreterTask()
data class TasksPipeline(val tasks: List<TaskInfo>) : InterpreterTask()


/**
 * This class represents executable name and its arguments.
 */
data class TaskInfo(val taskName: String, val arguments: List<String> = emptyList())
