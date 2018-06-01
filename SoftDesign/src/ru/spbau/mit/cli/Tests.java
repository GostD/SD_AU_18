package ru.spbau.mit.cli;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class Tests {
    public static boolean test(String text, String result) {
        String out = "";
        try (PipedOutputStream pout = new PipedOutputStream(); PipedInputStream pin = new PipedInputStream();) {
            pout.connect(pin);
            pout.write(text.getBytes());
            pout.flush();
            out = Interpretator.Interpret(pin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.equals(result);
    }

    @Test
    public void echoTest() {
        assertTrue(test("echo 1\n", "1"));
        assertTrue(test("c=5 | echo $c\n", "5"));
        assertTrue(test("c=3 | echo $c\n", "3"));
        assertTrue(test("echo $c | cat\n", "3"));
    }

    @Test
    public void pwd() {
        assertTrue(test("pwd\n", System.getProperty("user.dir")));
    }

    @Test
    public void catTest() {
        assertTrue(test("pwd\n | cat", System.getProperty("user.dir")));
    }

    @Test
    public void wcTest() {
        assertTrue(test("wc src/ru/spbau/mit/cli/Arg.java\n", "12 30 245"));
    }

}
