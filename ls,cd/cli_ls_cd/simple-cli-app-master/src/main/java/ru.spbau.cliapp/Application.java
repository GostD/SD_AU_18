package ru.spbau.cliapp;

import ru.spbau.cliapp.core.TaskInfo;
import ru.spbau.cliapp.core.Workflow;
import ru.spbau.cliapp.task.Cat;
import ru.spbau.cliapp.task.Echo;
import ru.spbau.cliapp.task.Task;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Application {
    public static void main(String[] args) throws IOException {
        Cat cat = new Cat();
        Echo echo = new Echo();
        Map<String, Task> namedTasks = new HashMap<>();
        namedTasks.put("cat", cat);
        namedTasks.put("echo", echo);

        Workflow workflow = new Workflow(
            namedTasks, Arrays.asList(
                new TaskInfo("echo", Arrays.asList("hello", "world")),
                new TaskInfo("cat"),
                new TaskInfo("cat")
            )
        );

        workflow.execute(System.in, System.out);
    }
}
