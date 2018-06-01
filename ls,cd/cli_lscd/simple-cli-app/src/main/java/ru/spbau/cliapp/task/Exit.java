package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.ProcessContext;
import ru.spbau.cliapp.core.StopInterpreterException;

import java.util.List;

public class Exit implements Task {
    @Override
    public int main(ProcessContext e, List<String> args) {
        throw new StopInterpreterException("Exit is called");
    }
}
