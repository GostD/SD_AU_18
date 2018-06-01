package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.StopInterpreterException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Exit implements Task {
    @Override
    public int main(InputStream stdin, OutputStream stdout, List<String> args) {
        throw new StopInterpreterException("Exit is called");
    }
}
