package ru.spbau.cliapp.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.spbau.cliapp.core.BasicProcessContext;
import ru.spbau.cliapp.core.ProcessContext;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static ru.spbau.cliapp.task.TestUtil.createInput;

@RunWith(MockitoJUnitRunner.class)
public class LsTest {

    @Mock
    private ProcessContext processContext;

    @Mock
    private ProcessContext processContext2;

    @Test
    public void ls_test() {
        String path = System.getProperty("user.dir");
        InputStream input = createInput("");
        OutputStream output = new ByteArrayOutputStream();
        OutputStream err = new ByteArrayOutputStream();
        Path workingDir = Paths.get(path);
        processContext = new BasicProcessContext(workingDir, input, output, err);
        OutputStream output2 = new ByteArrayOutputStream();
        processContext2 = new BasicProcessContext(workingDir, input, output2, err);
        new Ls().main(processContext, emptyList());
        new Ls().main(processContext2, singletonList(path));
        assertEquals(output.toString(), output2.toString());
    }
}