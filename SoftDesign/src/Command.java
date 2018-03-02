import java.util.List;
import java.io.*;

public class Command {//Базовый класс команд
    PipedInputStream execute(List<Argument> args, PipedInputStream  pin) throws FileNotFoundException {return null;}//Метод выполнения команды, возвращающий поток
}
