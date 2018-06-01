package ru.spbau.cliapp.interpreter

import ru.spbau.cliapp.core.InterpreterTask
import ru.spbau.cliapp.core.TaskInfo
import ru.spbau.cliapp.core.TasksPipeline
import ru.spbau.cliapp.parsing.*

/**
 * This class coordinates all parsing utils in order to convert input string into an actionable [InterpreterTask].
 *
 * It is supposed to be used directly in the [Interpreter] class.
 */
object InterpreterParser {

    private val tokenizer = Tokenizer
    private val interpolator = StringInterpolator
    private val taskInfoParser = TaskInfoParser
    private val assignmentParser = VarAssignmentInfoParser

    /**
     * Main method for parsing strings.
     *
     * Aware of environment variables and performs one pass of substitution.
     *
     * Performs substitution only for strings in double quotes and in tokens itself; in single-quoted strings
     * substitution is not performed.
     *
     * Examples of parsing:
     *
     * ```
     * "echo hello world", {}                   -> [TasksPipeline] {"echo", "hello", "world"}
     * "echo $bob world", {"bob" -> "john" }    -> [TasksPipeline] {"echo", "john", "world"}
     * "x=5", {}                                -> [VarAssignmentInfo] {"x", "5"}
     * ```
     */
    fun parse(input: String, env: Map<String, String>): InterpreterTask {
        val interpolatedTokens = tokenizer.tokenize(input).flatMap { interpolate(it, env) }
        if (isAssignment(interpolatedTokens)) {
            return assignmentParser.parse(interpolatedTokens)
        }

        return TasksPipeline(taskInfoParser.parse(interpolatedTokens))
    }

    private fun isAssignment(interpolatedTokens: List<Token>) =
            interpolatedTokens.size == 3 && interpolatedTokens[1] === Equals && interpolatedTokens[0] is StringToken

    private fun interpolate(token: Token, env: Map<String, String>) = when (token) {
        is DoubleQuotesToken -> listOf(DoubleQuotesToken(interpolator.interpolate(token.value, env)))
        is StringToken -> tokenizer.tokenize(interpolator.interpolate(token.value, env))
        else -> listOf(token)
    }
}