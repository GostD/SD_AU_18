package ru.spbau.cliapp.parsing

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TokenizerTest {
    val tokenizer = Tokenizer

    @Test
    fun `empty string tokenized to empty list`() {
        assertThat(tokenizer.tokenize("")).isEmpty()
    }

    @Test
    fun `one command is tokenized into string token`() {
        assertThat(tokenizer.tokenize("echo")).containsExactly(StringToken("echo"))
    }

    @Test
    fun `many commands are tokenised into correct tokens`() {
        assertThat(tokenizer.tokenize("echo hello one two"))
                .containsExactly(StringToken("echo"), StringToken("hello"), StringToken("one"), StringToken("two"))

    }

    @Test
    fun `many spaces between words ignored`() {
        assertThat(tokenizer.tokenize("     echo     hello    "))
                .containsExactly(StringToken("echo"), StringToken("hello"))
    }

    @Test
    fun `double quotes string is found`() {
        assertThat(tokenizer.tokenize("\"echo\"")).containsExactly(DoubleQuotesToken("echo"))
    }

    @Test
    fun `pipe symbol is recognized`() {
        assertThat(tokenizer.tokenize("|")).containsExactly(VerticalBar)
    }

    @Test
    fun `pipe is recognised with other things`() {
        assertThat(tokenizer.tokenize("echo \"hello world\" | cat '-v'"))
                .containsExactly(
                        StringToken("echo"),
                        DoubleQuotesToken("hello world"),
                        VerticalBar,
                        StringToken("cat"),
                        SingleQuotesToken("-v")
                )
    }

    @Test
    fun `pipes are recognised even when standing close to words`() {
        assertThat(tokenizer.tokenize("echo|cat|hello||"))
                .containsExactly(
                        StringToken("echo"),
                        VerticalBar,
                        StringToken("cat"),
                        VerticalBar,
                        StringToken("hello"),
                        VerticalBar,
                        VerticalBar
                )
    }

    @Test
    fun `pipes are not extracted from quoted tokens`() {
        assertThat(tokenizer.tokenize("'echo|pipe' \"double|quoted\""))
                .containsExactly(
                        SingleQuotesToken("echo|pipe"),
                        DoubleQuotesToken("double|quoted")
                )
    }

    @Test
    fun `equals sign is recognised`() {
        assertThat(tokenizer.tokenize("=")).containsExactly(Equals)
    }
}