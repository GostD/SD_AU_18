package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.ProcessContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Cat implements Task {

    @Override
    public int main(ProcessContext context, List<String> args) {
        if (args.isEmpty()) {
            return printFromStdout(context);
        } else {
            return printFiles(context, args);
        }

    }

    private int printFiles(ProcessContext context, List<String> args) {
        int errorCode = 0;
        for (String fileName : args) {
            try {
                Path path = context.getWorkingDir().resolve(fileName);
                byte[] bytes = Files.readAllBytes(path);
                context.getStdout().write(bytes);
                context.getStdout().write("\n".getBytes());
            } catch (IOException e) {
                errorCode = 1;
                try {
                    context.getErr().write(("Error priniting file " + fileName + "\n").getBytes());
                } catch (IOException ignored) {
                }
            }
        }

        return errorCode;
    }

    private int printFromStdout(ProcessContext context) {
        int readByte;
        try {
            while ((readByte = context.getStdin().read()) != -1) {
                context.getStdout().write(readByte);
            }
        } catch (IOException e) {
            return 1;
        }

        return 0;
    }
}
