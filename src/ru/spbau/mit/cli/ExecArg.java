package ru.spbau.mit.cli;

/**Аргументы которые требуют вычисления*/
public class ExecArg extends Argument {
    public ExecArg(String str) {
        super(str);
    }

    /**Строковое представление аргумента*/
    public String getValue() {
        if (Interpretator.variables.containsKey(arg)) {
            Argument argm = Interpretator.variables.get(arg);
            return argm.getValue();
        } else {
            return "";
        }
    }
}
