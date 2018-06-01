package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.ProcessContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Pwd implements Task {
    @Override
    public int main(ProcessContext context, List<String> args) {
        try {
            context.getStdout().write((context.getWorkingDir().toAbsolutePath().toString() + "\n").getBytes());
        } catch (IOException e) {
            return 1;
        }

        return 0;
    }

    @Override
    public int main(InputStream stdin, OutputStream stdout, List<String> args) {
        return 0;
    }
}
