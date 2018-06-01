package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.ProcessContext;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Echo implements Task {

    @Override
    public int main(ProcessContext context, List<String> args) {
        try {
            String result = args.stream().collect(Collectors.joining(" ")).concat("\n");
            context.getStdout().write(result.getBytes());
        } catch (IOException e) {
            return 1;
        }

        return 0;
    }
}
