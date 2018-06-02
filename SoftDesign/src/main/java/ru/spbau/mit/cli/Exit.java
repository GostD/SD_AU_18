package ru.spbau.mit.cli;
import java.io.PipedInputStream;
import java.util.List;

public class Exit implements Command {
    public PipedInputStream execute(List<Argument> args, PipedInputStream pin) {
        System.exit(0);
        return null;
    }
}
