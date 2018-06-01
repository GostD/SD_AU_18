package ru.spbau.cliapp.task;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.spbau.cliapp.core.ProcessContext;

import java.io.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static ru.spbau.cliapp.task.TestUtil.createFileWithText;
import static ru.spbau.cliapp.task.TestUtil.createInput;

/**
 * All expected data for this test is taken from the actual wc.
 */
@RunWith(MockitoJUnitRunner.class)
public class WcTest {
    private Wc wc = new Wc();

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Mock
    private ProcessContext context;

    @Test
    public void empty_string_gives_correct_answer() {
        InputStream emptyStringInput = createInput("");
        OutputStream output = new ByteArrayOutputStream();
        when(context.getStdin()).thenReturn(emptyStringInput);
        when(context.getStdout()).thenReturn(output);

        wc.main(context, emptyList());

        assertEquals("0 0 0\n", output.toString());
    }

    @Test
    public void hello_world_string_gives_correct_result() {
        InputStream input = createInput("hello world");
        OutputStream output = new ByteArrayOutputStream();
        when(context.getStdin()).thenReturn(input);
        when(context.getStdout()).thenReturn(output);

        wc.main(context, emptyList());

        assertEquals("0 2 11\n", output.toString());
    }

    @Test
    public void newlines_are_correctly_counted() {
        InputStream input = createInput("hello\nworld\n");
        OutputStream output = new ByteArrayOutputStream();
        when(context.getStdin()).thenReturn(input);
        when(context.getStdout()).thenReturn(output);

        wc.main(context, emptyList());

        assertEquals("2 2 12\n", output.toString());
    }

    @Test
    public void wc_reads_passed_file() throws IOException {
        File file = createFileWithText(tmpFolder, "expected string\nwith enters\n\nand stuff\n");
        OutputStream output = new ByteArrayOutputStream();
        when(context.getStdout()).thenReturn(output);
        when(context.getWorkingDir()).thenReturn(file.toPath().getParent());

        wc.main(context, singletonList(file.getName()));

        assertEquals("4 6 39\n", output.toString());
    }

}