package ru.spbau.mit.cli;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;

public class Eq implements Command {

    /**Метод выполняющий команду для входного потока pin, возвращает поток*/
    public PipedInputStream execute(List<Argument> args, PipedInputStream pin) {
        PipedInputStream res = new PipedInputStream();
        try (PipedOutputStream pout = new PipedOutputStream()) {
            pout.connect(res);
            Interpretator.variables.put(args.get(0).arg, args.get(1));
        } catch (IOException e) {
            throw new Error("Pipe error");
        }
        return res;
    }
}
