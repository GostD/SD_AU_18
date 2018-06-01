package ru.spbau.cliapp.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * This class represents context of process execution. Basically it's just a bundle of resources.
 */
public interface ProcessContext {
    InputStream getStdin();
    OutputStream getStdout();
    OutputStream getErr();
    Path getWorkingDir();
}
