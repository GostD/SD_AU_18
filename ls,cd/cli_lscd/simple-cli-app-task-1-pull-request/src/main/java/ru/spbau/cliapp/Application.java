package ru.spbau.cliapp;

import ru.spbau.cliapp.interpreter.Interpreter;
import ru.spbau.cliapp.interpreter.InterpreterParser;
import ru.spbau.cliapp.interpreter.TasksRegistry;
import ru.spbau.cliapp.parsing.StringInterpolator;
import ru.spbau.cliapp.parsing.TaskInfoParser;
import ru.spbau.cliapp.parsing.Tokenizer;
import ru.spbau.cliapp.task.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Application {
    private static final Map<String, Task> namedTasks = new HashMap<>();

    static {
        namedTasks.put("cat", new Cat());
        namedTasks.put("echo", new Echo());
        namedTasks.put("exit", new Exit());
        namedTasks.put("pwd", new Pwd());
        namedTasks.put("wc", new Wc());
    }

    public static void main(String[] args) throws IOException {
        InterpreterParser interpreterParser = InterpreterParser.INSTANCE;
        TasksRegistry taskRegistry = new TasksRegistry(NativeProcess::new, namedTasks);
        Interpreter interpreter = new Interpreter(Paths.get(""), taskRegistry, interpreterParser);

        interpreter.run(System.in, System.out, System.err);
    }
}
