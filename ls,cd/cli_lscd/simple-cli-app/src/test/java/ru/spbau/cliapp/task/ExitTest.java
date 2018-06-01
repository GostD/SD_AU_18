package ru.spbau.cliapp.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.spbau.cliapp.core.StopInterpreterException;

class ExitTest {

    @Test
    void exit_command_throws_StopInterpreterException() {
        Exit exit = new Exit();
        Assertions.assertThrows(StopInterpreterException.class,
            () -> exit.main(null, null));
    }
}