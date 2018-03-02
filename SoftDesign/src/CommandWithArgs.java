import java.util.List;

public class CommandWithArgs {//Класс содержащий команду с ее аргументами
    CommandWithArgs(Command cmd, List<Argument> arg) {
        command = cmd;
        args = arg;
    }
    Command command;
    List<Argument> args;
}
