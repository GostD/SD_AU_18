package ru.spbau.cliapp.task;

import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class TestUtil {
    private TestUtil() {
    }

    static public InputStream createInput(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    static public File createFileWithText(TemporaryFolder tmpFolder, String expectedString) throws IOException {
        File file = tmpFolder.newFile();
        Files.write(file.toPath(), expectedString.getBytes());
        return file;
    }


}
