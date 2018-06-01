package ru.spbau.cliapp.task;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import ru.spbau.cliapp.core.ProcessContext;

import java.io.IOException;
import java.util.List;

public class NativeProcess implements Task {

    private final String taskName;

    public NativeProcess(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public int main(ProcessContext context, List<String> args) {
        try {
            return executeNativeProcess(context, args);
        } catch (IOException e) {
            tryToPrintError(context, e);
            return 1;
        }
    }

    private int executeNativeProcess(ProcessContext context, List<String> args) throws IOException {
        DefaultExecutor defaultExecutor = new DefaultExecutor();

        CommandLine commandLine = new CommandLine(taskName).addArguments(args.toArray(new String[args.size()]));
        defaultExecutor.setStreamHandler(new PumpStreamHandler(context.getStdout(), context.getErr(), context.getStdin()));
        defaultExecutor.execute(commandLine);

        return 0;
    }

    private void tryToPrintError(ProcessContext context, IOException e) {
        try {
            context.getErr().write((e.getMessage() + "\n").getBytes());
        } catch (IOException ignored) {
        }
    }

}
