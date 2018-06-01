package ru.spbau.cliapp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Util class with methods for IO.
 */
public final class IOUtil {
    private IOUtil() {
    }

    /**
     * Wraps input stream and prevents its closing.
     */
    static public InputStream ignoringClose(InputStream inputStream) {
        return new NonClosingInputStream(inputStream);
    }

    /**
     * Wraps output stream and prevents its closing.
     */
    static public OutputStream ignoringClose(OutputStream outputStream) {
        return new NonClosingOutputStream(outputStream);
    }

    private static class NonClosingInputStream extends InputStream {
        private final InputStream original;

        private NonClosingInputStream(InputStream original) {
            this.original = original;
        }

        @Override
        public int read() throws IOException {
            return original.read();
        }

        @Override
        public void close() {}
    }

    private static class NonClosingOutputStream extends OutputStream {
        private final OutputStream original;

        private NonClosingOutputStream(OutputStream original) {
            this.original = original;
        }

        @Override
        public void write(int i) throws IOException {
            original.write(i);
        }

        @Override
        public void close() {}
    }
}
