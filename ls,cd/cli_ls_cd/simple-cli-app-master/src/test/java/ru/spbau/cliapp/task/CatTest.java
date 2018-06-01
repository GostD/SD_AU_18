package ru.spbau.cliapp.task;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.cliapp.core.BasicProcessContext;
import ru.spbau.cliapp.core.ProcessContext;

import java.io.*;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static ru.spbau.cliapp.task.TestUtil.createFileWithText;
import static ru.spbau.cliapp.task.TestUtil.createInput;

public class CatTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    private Cat cat = new Cat();

    @Test
    public void cat_prints_from_stdin_to_stdout_when_no_args() {
        String original = "hello, world!";
        InputStream input = createInput(original);
        OutputStream output = new ByteArrayOutputStream();

        cat.main(input, output, Collections.emptyList());

        assertEquals(original, output.toString());
    }

    @Test
    public void cat_with_arguments_opens_file_and_prints_it_adding_last_newline() throws IOException {
        String expectedString = "expected string\nwith enters\n\nand stuff\n";
        File file = createFileWithText(tmpFolder, expectedString);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ProcessContext processContext = new BasicProcessContext(file.toPath().getParent(), createInput(""), output, null);

        cat.main(processContext, Collections.singletonList(file.getName()));

        assertEquals(expectedString + "\n", output.toString());
    }

}