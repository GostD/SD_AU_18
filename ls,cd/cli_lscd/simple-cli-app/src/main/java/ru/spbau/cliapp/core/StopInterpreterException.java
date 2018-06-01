package ru.spbau.cliapp.core;

/**
 * This exception represents interpreter shutdown and should be thrown only from built-ins like `exit` command.
 */
public class StopInterpreterException extends RuntimeException {
    public StopInterpreterException(String s) {
        super(s);
    }
}
