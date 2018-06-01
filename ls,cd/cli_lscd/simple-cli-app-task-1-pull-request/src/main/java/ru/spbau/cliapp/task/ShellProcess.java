package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.ProcessContext;

import java.io.IOException;
import java.util.List;

final public class ShellProcess {
    private final Task task;

    private ShellProcess(Task task) {
        this.task = task;
    }

    public int execute(ProcessContext context, List<String> args) {
        int exitCode = task.main(context, args);
        try {
            context.getStdout().close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot close output stream", e);
        }
        return exitCode;
    }

    public static ShellProcess createProcess(Task task) {
        return new ShellProcess(task);
    }
}
