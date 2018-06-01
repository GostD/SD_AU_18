package ru.spbau.cliapp.parsing

import ru.spbau.cliapp.core.TaskInfo

/**
 * This class takes a list of [Token] objects and converts them into [TaskInfo] objects.
 *
 * This class does not perform any kind of substitution. However, it should throw exceptions if there are
 * forbidden combination of tokens (for example, two pipes in a row).
 *
 * Any string token is treated like command. So, for example, tokens `["echo", "hello"]` will be parsed
 * into single `TaskInfo` with command `"echo"` and a single argument `"hello"`.
 */
object TaskInfoParser {

    /**
     * Takes list of tokens, groups them by pipe symbols (`|`) and then transforms them into TaskInfos.
     *
     * @throws IllegalArgumentException if there are two pipe symbols standing together (like in `"hello | | there"`)
     */
    fun parse(tokens: List<Token>): List<TaskInfo> {
        if (tokens.isEmpty()) return emptyList()

        val splitByPipes = tokens.splitOn { it == VerticalBar }
        return assertNoEmptyTasks(splitByPipes)
                .map { formTaskInfo(it) }
    }

    private fun assertNoEmptyTasks(splittedByPipes: List<List<Token>>): List<List<Token>> {
        if (splittedByPipes.none { it.isEmpty() }) {
            return splittedByPipes
        }

        throw IllegalArgumentException("Two pipes are standing together!")
    }

    private fun formTaskInfo(tokens: List<Token>) = TaskInfo(tokens.first().value, tokens.tail().map { it.value })

}

private fun <T> List<T>.splitOn(condition: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<MutableList<T>>()
    result.add(mutableListOf())

    this.forEach {
        when {
            condition(it) -> result.add(mutableListOf())
            else -> result.last().add(it)
        }
    }

    return result
}

private fun <T> List<T>.tail() = drop(1)
