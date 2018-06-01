package ru.spbau.cliapp.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class BasicProcessContext implements ProcessContext {
    private Path workingDir;
    private InputStream stdin;
    private OutputStream stdout;
    private OutputStream stderr;

    public BasicProcessContext(Path workingDir, InputStream stdin, OutputStream stdout, OutputStream stderr) {
        this.workingDir = workingDir;
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    @Override
    public InputStream getStdin() {
        return stdin;
    }

    @Override
    public OutputStream getStdout() {
        return stdout;
    }

    @Override
    public OutputStream getErr() {
        return stderr;
    }

    @Override
    public Path getWorkingDir() {
        return workingDir;
    }

    @Override
    public void setWorkingDir(Path path) { workingDir = path; }
}
