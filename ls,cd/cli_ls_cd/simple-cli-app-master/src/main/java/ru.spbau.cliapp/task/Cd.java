package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.ProcessContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Cd implements Task {
    @Override
    public int main(ProcessContext context, List<String> args) {
        if (args.isEmpty()) {
            Path path = Paths.get(System.getProperty("user.dir"));
            context.setWorkingDir(path);
        } else {
            Path path = Paths.get(args.get(0));
            if (Files.exists(path)) {
                context.setWorkingDir(path);
            } else {
                Path tmp = Paths.get(context.getWorkingDir().getFileName() + args.get(0));
                if (Files.exists(tmp)) {
                    context.setWorkingDir(tmp);
                } else {
                    try {
                        context.getStdout().write(("cd: " + args.get(0) + ": No such file directory" + "\n").getBytes());
                    } catch (IOException e) {
                        return 1;
                    }
                }

            }

        }
//        try {
//            context.getStdout().write((context.getWorkingDir().toAbsolutePath().toString() + "\n").getBytes());
//        } catch (IOException e) {
//            return 1;
//        }

        return 0;
    }

    @Override
    public int main(InputStream stdin, OutputStream stdout, List<String> args) {
        return 0;
    }
}