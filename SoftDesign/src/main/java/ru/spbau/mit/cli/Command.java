package ru.spbau.mit.cli;
import java.util.List;
import java.io.*;

/**Базовый класс команд*/
public interface Command {

    /**Метод выполнения команды, возвращающий поток*/
    PipedInputStream execute(List<Argument> args, PipedInputStream  pin) throws FileNotFoundException;
}
