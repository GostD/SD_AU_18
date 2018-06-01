package ru.spbau.cliapp.parsing

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.spbau.cliapp.core.VarAssignmentInfo

class VarAssignmentInfoParserTest {
    val parser = VarAssignmentInfoParser

    @Test
    fun `empty list is not parsed`() {
        assertThrows<IllegalArgumentException> {
            parser.parse(emptyList())
        }
    }

    @Test
    fun `more than three arguments list is not parsed`() {
        assertThrows<IllegalArgumentException> {
            parser.parse(listOf(StringToken("1"), StringToken("2"), StringToken("3"), StringToken("4")))
        }
    }

    @Test
    fun `three arguments without assignment sign in middle are not parsed`() {
        assertThrows<IllegalArgumentException> {
            parser.parse(listOf(StringToken("x"), StringToken("y"), StringToken("z")))
        }

        assertThrows<IllegalArgumentException> {
            parser.parse(listOf(StringToken("x"), StringToken("y"), StringToken("=")))
        }

        assertThrows<IllegalArgumentException> {
            parser.parse(listOf(StringToken("="), StringToken("y"), StringToken("z")))
        }
    }

    @Test
    fun `left part of assignment can be only string token`() {
        assertThrows<IllegalArgumentException> {
            parser.parse(listOf(DoubleQuotesToken("varName"), Equals, StringToken("value")))
        }

        assertThrows<IllegalArgumentException> {
            parser.parse(listOf(SingleQuotesToken("varName"), Equals, StringToken("value")))
        }

        assertThrows<IllegalArgumentException> {
            parser.parse(listOf(VerticalBar, Equals, StringToken("value")))
        }

    }

    @Test
    fun `valid assignment with string tokens is parsed`() {
        val assignment = listOf(StringToken("varName"), Equals, StringToken("value"))

        assertThat(parser.parse(assignment))
                .isEqualTo(VarAssignmentInfo("varName", "value"))
    }
}
