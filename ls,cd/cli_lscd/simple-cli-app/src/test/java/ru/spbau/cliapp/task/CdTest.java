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
public class CdTest {

    @Mock
    private ProcessContext processContext;

    @Test
    public void cd_test() {
        String changePath = System.getProperty("user.dir");//"/Users/selp/Downloads";
        String[] init = changePath.split("/");
        String initializePath = "";
        for (int i = 0; i < init.length - 1; i++) {
            initializePath += "/" + init[i];
        }
        InputStream input = createInput("");
        OutputStream output = new ByteArrayOutputStream();
        OutputStream err = new ByteArrayOutputStream();
        Path workingDir = Paths.get(initializePath);
        processContext = new BasicProcessContext(workingDir, input, output, err);
        new Cd().main(processContext, singletonList(changePath));
        new Pwd().main(processContext, emptyList());
        assertEquals(changePath + "\n", output.toString());
    }
}