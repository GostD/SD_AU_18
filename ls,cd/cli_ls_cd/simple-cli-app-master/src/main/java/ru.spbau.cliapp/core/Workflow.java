package ru.spbau.cliapp.core;

import ru.spbau.cliapp.task.ShellProcess;
import ru.spbau.cliapp.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Workflow {
    private final List<TaskInfo> tasks;
    private final Map<String, Task> namedTasks;

    public Workflow(Map<String, Task> namedTasks, List<TaskInfo> tasksToRun) {
        this.tasks = tasksToRun;
        this.namedTasks = namedTasks;
    }

    public void execute(InputStream stdin, OutputStream stdout) throws IOException {
        List<ShellProcess> processes = tasks.stream()
            .map(t -> t.taskName)
            .map(namedTasks::get)
            .map(ShellProcess::createProcess)
            .collect(Collectors.toList());

        List<Pipe> pipes = new ArrayList<>();
        for (int i = 0; i < tasks.size() - 1; i++) {
            pipes.add(new SystemPipe());
        }

        List<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(stdin);
        inputStreams.addAll(pipes.stream().map(Pipe::getInputStream).collect(Collectors.toList()));

        List<OutputStream> outputStreams = pipes.stream()
            .map(Pipe::getOutputStream)
            .collect(Collectors.toCollection(ArrayList::new));
        outputStreams.add(stdout);

        for (int i = 0; i < processes.size(); i++) {
            ShellProcess process = processes.get(i);
            TaskInfo task = tasks.get(i);
            InputStream in = inputStreams.get(i);
            OutputStream out = outputStreams.get(i);
            process.execute(in, out, task.arguments);
        }
    }
}
