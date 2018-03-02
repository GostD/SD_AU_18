import java.util.List;
import java.util.ArrayList;

public class Parser {
    Parser(String str) {
        commandLine = str;
    }
    List<CommandWithArgs> getCommands() throws IllegalArgumentException {//Метод разбирающий строку входной команды и возвращающий список команд и аргументов
        List<CommandWithArgs> list = new ArrayList<>();
        int len = commandLine.length();
        String cmd = "";
        StringBuilder cmdBuild = new StringBuilder();
        List<Argument> args = new ArrayList<>();
        String arg = "";
        StringBuilder argBuild = new StringBuilder();
        boolean comScan = true;
        for (int i = 0; i < len; i++) {
            char current = commandLine.charAt(i);
            if (current == '|') {
                if (!comScan)  {
                    if (argBuild.length() != 0) {
                        args.add(new Arg(argBuild.toString()));
                        argBuild = new StringBuilder();
                    }
                }
                list.add(new CommandWithArgs(buildCommand(cmd), args));
                cmd = "";
//                arg = "";
                args = new ArrayList<>();
                comScan = true;
                i++;
                while (commandLine.charAt(i) == ' ') {
                    i++;
                }
                i--;
            } else if (current == ' ') {
                if (comScan) {
                    comScan = false;
                    cmd = cmdBuild.toString();
                    cmdBuild = new StringBuilder();
                } else {
                    if (argBuild.length() != 0) {
                        args.add(new Arg(argBuild.toString()));
                        argBuild = new StringBuilder();
                    }
                }
            } else if (current == '=') {
                if (comScan) {
                    args.add(new Arg(cmdBuild.toString()));
                    cmdBuild = new StringBuilder();
                    cmd = "=";
                    comScan = false;
                } else {
                    throw new IllegalArgumentException("Wrong argument");
                }
            } else if (current == '$') {
                args.add(parseExecArg(i + 1));
                arg = "";
                i = helpInd - 1;
                if (i == len - 1) {
                    list.add(new CommandWithArgs(buildCommand(cmd), args));
                }
            } else if ((current == '"') || (current == '\'')) {
                arg = parseStringArg(i + 1, current);
                i = helpInd - 1;
                args.add(new StringArg(arg));
                arg = "";
                if (i == len - 1) {
                    list.add(new CommandWithArgs(buildCommand(cmd), args));
                }
            } else if (i == len - 1) {
                if (comScan) {
                    cmdBuild.append(current);
                    list.add(new CommandWithArgs(buildCommand(cmdBuild.toString()), args));
                } else {
                    argBuild.append(current);
                    args.add(new Arg(argBuild.toString()));
                    list.add(new CommandWithArgs(buildCommand(cmd), args));
                }
            } else {
                if (comScan) {
                    cmdBuild.append(current);
                } else {
                    argBuild.append(current);
                }
            }
        }
        return list;
    }
    private String parseStringArg(int i, char current) throws IllegalArgumentException {//Метод считывающий аргумент-строку, current обрамляющий символ
        char cur = commandLine.charAt(i);
        StringBuilder build = new StringBuilder();
        if (current == '\'') {
            while (cur != current) {
                build.append(cur);
                i++;
                cur = commandLine.charAt(i);
            }
        } else {
            while (cur != current) {
                if (cur == '$') {
                    String res = parseExecArg(i + 1, current);
                    build.append(res);
                    i = helpInd - 1;

                } else {
                    build.append(cur);
                    i++;
                }
                cur = commandLine.charAt(i);
            }
        }

        helpInd = i + 1;
        return build.toString();
    }
    private Argument parseExecArg(int i) {//Метод обрабатывающий аргумент требующий вычисления
        char cur = commandLine.charAt(i);
        String res;
        if (cur == '"' | cur == '\'') {
            res = parseStringArg(i + 1, cur);
            return new StringArg(res);
        } else {
            StringBuilder build = new StringBuilder();
            while (cur != ' ') {
                build.append(cur);
                i++;
                if (i == commandLine.length()) {
                    helpInd = i;
                    return new ExecArg(build.toString());
                }
                cur = commandLine.charAt(i);
            }
            helpInd = i+1;
            return new ExecArg(build.toString());
        }
    }
    private String parseExecArg(int i, char current) throws IllegalArgumentException {//Метод обрабатывающий аргумент требующий вычисления находящийся в строке
        char cur = commandLine.charAt(i);
        if (cur == current) {
            helpInd = i + 1;
            return "$";
        } else {
            StringBuilder build = new StringBuilder();
            while (cur != ' ' && cur != current) {
                build.append(cur);
                i++;
                if (i == commandLine.length()) {
                    throw new IllegalArgumentException("Wrong argument");
                }
                cur = commandLine.charAt(i);
            }
            helpInd = i + 1;
            return (new ExecArg(build.toString())).getValue();
        }
    }
    private Command buildCommand(String cmd) throws IllegalArgumentException {//Метод возвращающий класс комманды определенного типа
        if (cmd.equals("exit")) {
            return new Exit();
        } else if (cmd.equals("cat") | cmd.equals("wc")) {
            return new FileComm(cmd);
        } else if (cmd.equals("pwd")) {
            return new NoArgsComm(cmd);
        } else if (cmd.equals("echo")) {
            return new ArgsComm(cmd);
        } else if (cmd.equals("=")) {
            return new ArgsComm("=");
        } else {
            return new UnknownCommand(cmd);
        }
    }

    private String commandLine;
    private int helpInd;
}
