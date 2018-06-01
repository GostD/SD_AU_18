package ru.spbau.cliapp.parsing

/**
 * This class can insert variables into strings by their names mentioned with '$' sign.
 *
 * For example: `hello $world` -> `hello Bob`, if variable `world` equals Bob.
 */
object StringInterpolator {
    private val VAR_REGEX = Regex("\\\$([a-zA-Z][a-zA-Z0-9_]*)")

    /**
     * Main method for interpolation.
     *
     * It performs interpolation only once. So, if variable itself references another variable,
     * it will not be inserted.
     *
     * All missing symbols will be leaved as is, so if there is no variable `world` in [variables], it will not be
     * substituted.
     */
    fun interpolate(s: String, variables: Map<String, String>): String {
        return s.replace(VAR_REGEX, {
            variables.getOrDefault(it.groupValues[1], it.value)
        })
    }
}