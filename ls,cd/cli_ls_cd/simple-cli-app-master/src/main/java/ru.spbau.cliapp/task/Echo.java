package ru.spbau.cliapp.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Echo implements Task {

    @Override
    public int main(InputStream stdin, OutputStream stdout, List<String> args) {
        try {
            for (String arg : args) {
                stdout.write(arg.getBytes());
                stdout.write(" ".getBytes());
            }
        } catch (IOException e) {
            return 1;
        }

        return 0;
    }
}
