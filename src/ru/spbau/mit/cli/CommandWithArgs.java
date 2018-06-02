package ru.spbau.mit.cli;
import java.util.List;

/**Класс содержащий команду с ее аргументами*/
public class CommandWithArgs {
    public Command command;
    public List<Argument> args;

    public CommandWithArgs(Command cmd, List<Argument> arg) {
        command = cmd;
        args = arg;
    }
}
