package ru.spbau.cliapp.task;

import com.sun.javafx.PlatformUtil;
import org.junit.jupiter.api.Test;
import ru.spbau.cliapp.core.BasicProcessContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Paths;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

class NativeProcessTest {
    @Test
    void test_unix_cat_is_working() {
        assumeTrue(PlatformUtil.isUnix());

        ByteArrayInputStream input = new ByteArrayInputStream("hello world".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        BasicProcessContext context = new BasicProcessContext(Paths.get(""), input, output, null);

        new NativeProcess("cat")
            .main(context, Collections.emptyList());

        assertThat(output.toString()).isEqualTo("hello world");
    }
}