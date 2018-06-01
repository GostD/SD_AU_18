package ru.spbau.cliapp.core;

import ru.spbau.cliapp.interpreter.TasksRegistry;
import ru.spbau.cliapp.task.ShellProcess;
import ru.spbau.cliapp.util.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Workflow {
    private final List<TaskInfo> tasks;
    private final TasksRegistry namedTasks;

    public Workflow(TasksRegistry taskRegistry, List<TaskInfo> tasksToRun) {
        this.tasks = tasksToRun;
        this.namedTasks = taskRegistry;
    }

    /**
     * Connects tasks in workflow with pipes and invokes them sequentially.
     *
     * Does not close any of the passed streams.
     */
    public void execute(Path workingDir, InputStream stdin, OutputStream stdout, OutputStream stderr) throws IOException {
        List<ShellProcess> processes = tasks.stream()
            .map(TaskInfo::getTaskName)
            .map(namedTasks::getTaskByName)
            .map(ShellProcess::createProcess)
            .collect(Collectors.toList());

        List<Pipe> pipes = new ArrayList<>();
        for (int i = 0; i < tasks.size() - 1; i++) {
            pipes.add(new SystemPipe());
        }

        List<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(IOUtil.ignoringClose(stdin));
        inputStreams.addAll(pipes.stream().map(Pipe::getInputStream).collect(Collectors.toList()));

        List<OutputStream> outputStreams = pipes.stream()
            .map(Pipe::getOutputStream)
            .collect(Collectors.toCollection(ArrayList::new));
        outputStreams.add(IOUtil.ignoringClose(stdout));

        for (int i = 0; i < processes.size(); i++) {
            ShellProcess process = processes.get(i);
            TaskInfo task = tasks.get(i);
            InputStream in = inputStreams.get(i);
            OutputStream out = outputStreams.get(i);
            ProcessContext context = new BasicProcessContext(workingDir, in, out, stderr);
            process.execute(context, task.getArguments());
        }
    }
}
