package ru.spbau.cliapp.task;

import ru.spbau.cliapp.core.ProcessContext;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class Wc implements Task {
    @Override
    public int main(InputStream stdin, OutputStream stdout, List<String> args) {
        throw new NotImplementedException();
    }

    @Override
    public int main(ProcessContext context, List<String> args) {
        if (args.isEmpty()) {
            try {
                printlnWcResult(context.getStdout(), getCounts(context.getStdin()));
            } catch (IOException e) {
                return 1;
            }
        } else {
            for (String fileName : args) {
                Path file = context.getWorkingDir().resolve(fileName);
                try (InputStream fileInputStream = Files.newInputStream(file)) {
                    WcResult counts = getCounts(fileInputStream);
                    printlnWcResult(context.getStdout(), counts);
                } catch (IOException ignored) {
                }

            }
        }

        return 0;

    }

    private void printlnWcResult(OutputStream stdout, WcResult counts) throws IOException {
        stdout.write((counts + "\n").getBytes());
    }

    private WcResult getCounts(InputStream stdin) throws IOException {
        ByteArrayOutputStream bytesBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        WcResult result = new WcResult();

        int size;
        while ((size = stdin.read(buffer)) != -1) {
            bytesBuffer.write(buffer, 0, size);
        }

        String wholeString = bytesBuffer.toString();

        result.bytesCount = bytesBuffer.size();
        result.words = countWords(wholeString);
        result.newlines = countNewlines(wholeString);

        return result;
    }

    private long countNewlines(String result) {
        return result.chars().filter(c -> (char) c == '\n').count();
    }

    private long countWords(String result) {
        return Arrays.stream(result.split("\n"))
            .filter(s -> !s.isEmpty())
            .mapToLong(s -> s.split("\\s+").length)
            .sum();
    }

    private class WcResult {
        long newlines = 0;
        int bytesCount = 0;
        long words = 0;

        @Override
        public String toString() {
            return MessageFormat.format("{0} {1} {2}", newlines, words, bytesCount);
        }
    }
}
