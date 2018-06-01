package ru.spbau.cliapp.interpreter


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.spbau.cliapp.core.TaskInfo
import ru.spbau.cliapp.core.TasksPipeline
import ru.spbau.cliapp.core.VarAssignmentInfo

class InterpreterParserTest {
    val parser = InterpreterParser

    @Test
    fun `simple parsing example`() {
        val parsed = parser.parse("echo hello world | cat | wc", emptyMap()) as TasksPipeline

        assertThat(parsed.tasks)
                .containsExactly(
                        TaskInfo("echo", listOf("hello", "world")),
                        TaskInfo("cat"),
                        TaskInfo("wc")
                )
    }

    @Test
    fun `complex parsing exapmple`() {
        val parsed = parser.parse("echo \$x | \$wc", mapOf("x" to "hello | cat", "wc" to "less -f")) as TasksPipeline

        assertThat(parsed.tasks)
                .containsExactly(
                        TaskInfo("echo", listOf("hello")),
                        TaskInfo("cat"),
                        TaskInfo("less", listOf("-f"))
                )
    }

    @Test
    fun `expansion is performed in double qouted strings`() {
        val parsed = parser.parse("echo \"\$hello\"", mapOf("hello" to "world")) as TasksPipeline

        assertThat(parsed.tasks)
                .containsExactly(TaskInfo("echo", listOf("world")))
    }

    @Test
    fun `no expansion performed in single quoted strings`() {
        val parsed = parser.parse("echo '\$hello'", mapOf("hello" to "world")) as TasksPipeline

        assertThat(parsed.tasks)
                .containsExactly(TaskInfo("echo", listOf("\$hello")))
    }

    @Test
    fun `double quoted string is not splitted after expansion`() {
        val parsed = parser.parse("echo \"one \$hello world\"", mapOf("hello" to "world")) as TasksPipeline

        assertThat(parsed.tasks)
                .containsExactly(TaskInfo("echo", listOf("one world world")))
    }

    @Test
    fun `variable assignment is parsed correctly`() {
        val parsed = parser.parse("varName = hello", emptyMap()) as VarAssignmentInfo

        assertThat(parsed).isEqualTo(VarAssignmentInfo("varName", "hello"))
    }

    @Test
    fun `expansion is performed in variable assignment`() {
        val parser = parser.parse("varName=\$x", mapOf("x" to "value")) as VarAssignmentInfo

        assertThat(parser).isEqualTo(VarAssignmentInfo("varName", "value"))
    }
}