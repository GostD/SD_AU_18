package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.ProcessContext;

import java.util.List;

public interface Task {
    int main(ProcessContext context, List<String> args);
}
