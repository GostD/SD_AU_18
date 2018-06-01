package ru.spbau.cliapp.interpreter

import ru.spbau.cliapp.core.TasksPipeline
import ru.spbau.cliapp.core.VarAssignmentInfo
import ru.spbau.cliapp.core.Workflow
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.nio.file.Path
import java.util.*

/**
 * This class serves as a main controller which gathers all modules together
 */
class Interpreter(
        private val workingDir: Path,
        private val taskRegistry: TasksRegistry,
        private val parser: InterpreterParser) {

    private val environment = mutableMapOf<String, String>()

    /**
     * Starts interpreter and binds it to passed streams. It will stop when EOL will be entered directly into
     * stdin.
     *
     * Does not close any of passed streams.
     *
     * @param `input` stdin for shell
     * @param output stdout for shell
     * @param err stderr for shell
     * @throws IOException if anything goes wrong during tasks execution
     */
    @Throws(IOException::class)
    fun run(input: InputStream, output: OutputStream, err: OutputStream) {
        val scanner = Scanner(input)
        val w = PrintStream(output)

        printPreface(w)
        while (scanner.hasNextLine()) {
            val command = scanner.nextLine()

            val parsedResult = parser.parse(command, environment)
            when (parsedResult) {
                is TasksPipeline -> executePipeline(parsedResult, input, output, err)
                is VarAssignmentInfo -> environment[parsedResult.varName] = parsedResult.value
            }

            printPreface(w)
        }
    }

    private fun executePipeline(pipeline: TasksPipeline, input: InputStream, output: OutputStream, err: OutputStream) {
        Workflow(taskRegistry, pipeline.tasks).execute(workingDir, input, output, err)
    }

    private fun printPreface(w: PrintStream) {
        w.print(workingDir.toAbsolutePath().toString() + "> ")
    }
}
