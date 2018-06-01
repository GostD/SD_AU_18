package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.ProcessContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Ls implements Task {
    @Override
    public int main(ProcessContext context, List<String> args) {
        String str;
        if (args.isEmpty()) {
            str = context.getWorkingDir().toString();
        } else {
            str = args.get(0);
        }
        File folder = new File(str);
        File[] ls = folder.listFiles();
        for (File file : ls) {
            try {
                context.getStdout().write((file.getName() + '\n').getBytes());
            } catch (IOException e) {
                return 1;
            }
        }


        return 0;
    }

}