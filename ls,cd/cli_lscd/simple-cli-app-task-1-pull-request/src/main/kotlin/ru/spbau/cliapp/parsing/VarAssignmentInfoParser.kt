package ru.spbau.cliapp.parsing

import ru.spbau.cliapp.core.VarAssignmentInfo


/**
 * This class parses list of three vell-formed tokens into [VarAssignmentInfo].
 *
 * It throws [IllegalArgumentException] if cannot parse assignment.
 */
object VarAssignmentInfoParser {
    fun parse(tokens: List<Token>): VarAssignmentInfo {
        assertTokensFormCorrectAssignment(tokens)

        val varName = tokens[0]
        val valueToken = tokens[2]

        return VarAssignmentInfo(varName.value, valueToken.value)
    }

    private fun assertTokensFormCorrectAssignment(tokens: List<Token>) {
        if (tokens.size != 3) {
            throw IllegalArgumentException("Cannot parse token list $tokens assignment info, it is too large")
        }

        if (tokens[1] != Equals) {
            throw IllegalArgumentException("$tokens is not assignment; assignment sign should be in the middle")
        }

        if (tokens[0] !is StringToken) {
            throw IllegalArgumentException("Variable name can only be plain token, not ${tokens[0]}")
        }
    }
}