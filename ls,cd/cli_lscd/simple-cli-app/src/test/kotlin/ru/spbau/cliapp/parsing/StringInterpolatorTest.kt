package ru.spbau.cliapp.parsing

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StringInterpolatorTest {

    val interpolator = StringInterpolator

    @Test
    fun `empty string remains empty string`() {
        assertThat(interpolator.interpolate("", emptyMap())).isBlank()
    }

    @Test
    fun `text without variables is not changed`() {
        assertThat(interpolator.interpolate("text without vars", emptyMap()))
                .isEqualTo("text without vars")
    }

    @Test
    fun `when variable is not present it is leaved as is`() {
        assertThat(interpolator.interpolate("hello \$world", emptyMap()))
                .isEqualTo("hello \$world")
    }

    @Test
    fun `when variable is present in map it is replaced in string`() {
        assertThat(interpolator.interpolate("hello \$world", mapOf("world" to "Bob")))
                .isEqualTo("hello Bob")
    }

    @Test
    fun `same variable mentioned many times is interpolated`() {
        assertThat(interpolator.interpolate("say \$it hello to \$it", mapOf("it" to "now")))
                .isEqualTo("say now hello to now")
    }

    @Test
    fun `many different varables are interpolated`() {
        val variables = mapOf("one" to "1", "two" to "2", "three" to "3")

        assertThat(interpolator.interpolate("\$one \$two \$three", variables))
                .isEqualTo("1 2 3")
    }

    @Test
    internal fun `two variables in a row are correctly interpolated`() {
        val variables = mapOf("one" to "1", "two" to "2")

        assertThat(interpolator.interpolate("\$one\$two\$one", variables))
                .isEqualTo("121")
    }
}