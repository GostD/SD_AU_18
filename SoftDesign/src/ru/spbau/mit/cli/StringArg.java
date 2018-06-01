package ru.spbau.mit.cli;

/**Аргумент строка*/
public class StringArg extends Argument {
    public StringArg(String str) {
        arg = str;
    }

    /**Строковое представление*/
    public String getValue() {
        return arg;
    }
}
