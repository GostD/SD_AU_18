package ru.spbau.cliapp.parsing

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.spbau.cliapp.core.TaskInfo

class TaskInfoParserTest {

    val parser = TaskInfoParser

    @Test
    fun `empty list of tasks is returned on empty list of tokens`() {
        assertThat(parser.parse(emptyList())).isEmpty()
    }

    @Test
    fun `single string token is treated as command name`() {
        assertThat(parser.parse(listOf(StringToken("command"))))
                .containsExactly(TaskInfo("command"))
    }

    @Test
    fun `many string tokens in row are treated as command and its arguments`() {
        val tokensInRow = listOf(StringToken("command"), StringToken("arg1"), StringToken("arg2"), StringToken("arg3"))

        assertThat(parser.parse(tokensInRow))
                .containsExactly(TaskInfo("command", listOf("arg1", "arg2", "arg3")))
    }

    @Test
    fun `two string tokens separated by pipe are treated like two commands`() {
        val twoCommands = listOf(StringToken("cmd1"), VerticalBar, StringToken("cmd2"))

        assertThat(parser.parse(twoCommands)).containsExactly(
                TaskInfo("cmd1"),
                TaskInfo("cmd2")
        )
    }

    @Test
    fun `piped commands with arguments are correctly parsed`() {
        val firstCommand = listOf(StringToken("cmd1"), StringToken("arg1"))
        val secondCommand = listOf(StringToken("cmd2"), StringToken("arg2"))

        assertThat(parser.parse(firstCommand + VerticalBar + secondCommand)).containsExactly(
                TaskInfo("cmd1", listOf("arg1")),
                TaskInfo("cmd2", listOf("arg2"))
        )
    }

    @Test
    fun `parser throws exception on two pipes in the row`() {
        assertThrows<IllegalArgumentException> {
            parser.parse(listOf(VerticalBar, VerticalBar))
        }
    }
}