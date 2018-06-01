package ru.spbau.cliapp.task;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import ru.spbau.cliapp.core.ProcessContext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PwdTest {

    @Mock
    private ProcessContext processContext;

    @Test
    public void pwd_prints_directory_from_context_to_output() {
        OutputStream output = new ByteArrayOutputStream();
        Path workingDir = Paths.get("/some/test/path");
        when(processContext.getStdout()).thenReturn(output);
        when(processContext.getWorkingDir()).thenReturn(workingDir);

        new Pwd().main(processContext, Collections.emptyList());

        assertEquals(workingDir.toString() + "\n", output.toString());
    }
}