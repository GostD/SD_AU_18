package ru.spbau.mit.cli;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

public class Echo implements Command {

    /**Метод выполняющий команду для входного потока pin, возвращает поток*/
    public PipedInputStream execute(List<Argument> args, PipedInputStream pin) {
        PipedInputStream res = new PipedInputStream();
        try (PipedOutputStream pout = new PipedOutputStream()) {
            pout.connect(res);
            boolean start = true;
            for (Argument arg : args) {
                if (!start) pout.write(' ');
                start = false;
                pout.write((arg.getValue() + '\n').getBytes());
            }
            pout.flush();
        } catch (IOException e) {
            throw new Error("Pipe error");
        }
        return res;
    }
}
