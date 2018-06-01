package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.ProcessContext;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Task {
    @Deprecated
    int main(InputStream stdin, OutputStream stdout, List<String> args);

    default int main(ProcessContext context, List<String> args) {
        return main(context.getStdin(), context.getStdout(), args);
    }
}
