package ru.spbau.cliapp.task;

import org.junit.Test;
import ru.spbau.cliapp.core.StopInterpreterException;

public class ExitTest {

    @Test(expected = StopInterpreterException.class)
    public void exit_command_throws_StopInterpreterException() {
        Exit exit = new Exit();
        exit.main(null, null, null);
    }
}