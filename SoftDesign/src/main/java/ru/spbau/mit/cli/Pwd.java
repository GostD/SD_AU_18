package ru.spbau.mit.cli;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

public class Pwd implements Command {

    /**Метод выполняющий команду для входного потока pin, возвращает поток*/
    public PipedInputStream execute(List<Argument> args, PipedInputStream pin) {

        PipedInputStream res = new PipedInputStream();
        try (PipedOutputStream pout = new PipedOutputStream()) {
            try {
                pout.connect(res);
            } catch (IOException e) {
                throw new Error("Could not connect pipes");
            }
            pout.write((System.getProperty("user.dir") + '\n').getBytes());
        } catch (IOException e) {
            throw new Error("Pipe error");
        }
        return res;
    }
}
