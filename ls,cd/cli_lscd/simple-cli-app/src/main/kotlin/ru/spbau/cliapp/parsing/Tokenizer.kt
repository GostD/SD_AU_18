package ru.spbau.cliapp.parsing

/**
 * This is a base class for all possible tokens.
 *
 * [value] field represents piece of the original string.
 */
sealed class Token(open val value: String)
data class StringToken(override val value: String) : Token(value)
data class DoubleQuotesToken(override val value: String) : Token(value)
data class SingleQuotesToken(override val value: String) : Token(value)
object VerticalBar : Token("|")
object Equals : Token("=")

/**
 * This class splits string into tokens which can be analysed in parser.
 *
 * It is not supposed to perform any kind of substitution (like "$x" -> "echo").
 */
object Tokenizer {
    private val singleQuotes = "('[^']*')"
    private val doubleQuotes = "(\"[^\"]*\")"
    private val verticalBar = "(\\|)"
    private val equalsSign = "(=)"
    private val otherSymbols: String = "([^'\"|=]+)"
    private val splitRegex = Regex("${singleQuotes}|${doubleQuotes}|${verticalBar}|${equalsSign}|${otherSymbols}")
    private val singleQuotesRegex = Regex("'[^']*'")
    private val doubleQuotesRegex = Regex("\"[^\"]*\"")

    fun tokenize(s: String): List<Token> {
        return splitRegex.findAll(s)
                .map { it.value }
                .map { selectToken(it) }
                .flatMap { splitNonQuotedTokens(it) }
                .filter { it.value.isNotBlank() }
                .toList()
    }

    private fun splitNonQuotedTokens(it: Token) = when (it) {
        is StringToken -> it.value.split(" ").map { StringToken(it) }.asSequence()
        else -> sequenceOf(it)
    }

    private fun selectToken(token: String) = when {
        singleQuotesRegex matches token -> SingleQuotesToken(token.removeSurrounding("\'"))
        doubleQuotesRegex matches token -> DoubleQuotesToken(token.removeSurrounding("\""))
        token == "|" -> VerticalBar
        token == "=" -> Equals
        else -> StringToken(token)
    }

}