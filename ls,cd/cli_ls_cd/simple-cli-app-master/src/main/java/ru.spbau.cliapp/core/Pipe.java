package ru.spbau.cliapp.core;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

public interface Pipe extends Closeable {
    InputStream getInputStream();
    OutputStream getOutputStream();
}
