package ru.spbau.cliapp.core;

import java.io.*;

public class SystemPipe implements Pipe {

    private final PipedInputStream input;
    private final PipedOutputStream output;

    public SystemPipe() throws IOException {
        input = new PipedInputStream();
        output = new PipedOutputStream(input);
    }

    @Override
    public InputStream getInputStream() {
        return input;
    }

    @Override
    public OutputStream getOutputStream() {
        return output;
    }

    @Override
    public void close() throws IOException {
        output.close();
        input.close();
    }
}
